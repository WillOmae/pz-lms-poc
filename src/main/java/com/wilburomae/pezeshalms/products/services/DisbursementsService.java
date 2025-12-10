package com.wilburomae.pezeshalms.products.services;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountEntity;
import com.wilburomae.pezeshalms.accounts.data.entities.CurrencyEntity;
import com.wilburomae.pezeshalms.accounts.data.repositories.CurrencyRepository;
import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import com.wilburomae.pezeshalms.products.data.entities.*;
import com.wilburomae.pezeshalms.products.data.repositories.LoanProductRepository;
import com.wilburomae.pezeshalms.products.data.repositories.LoanRepository;
import com.wilburomae.pezeshalms.products.dtos.DisbursementRequest;
import com.wilburomae.pezeshalms.transactions.data.entities.ReasonTypeEntity;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionEntity;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionTypeEntity;
import com.wilburomae.pezeshalms.transactions.data.repositories.TransactionRepository;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionEntryRequest;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionRequest;
import com.wilburomae.pezeshalms.transactions.services.TransactionsUpsertService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
public class DisbursementsService implements UpsertService<DisbursementRequest> {

    private final LoanRepository loanRepository;
    private final LoanProductRepository loanProductRepository;
    private final TransactionRepository transactionRepository;
    private final CurrencyRepository currencyRepository;
    private final TransactionsUpsertService transactionsUpsertService;
    private final PaymentsService paymentsService;
    private final ReversalsService reversalsService;

    public DisbursementsService(LoanRepository loanRepository, LoanProductRepository loanProductRepository, TransactionRepository transactionRepository, CurrencyRepository currencyRepository, TransactionsUpsertService transactionsUpsertService, PaymentsService paymentsService, ReversalsService reversalsService) {
        this.loanRepository = loanRepository;
        this.loanProductRepository = loanProductRepository;
        this.transactionRepository = transactionRepository;
        this.currencyRepository = currencyRepository;
        this.transactionsUpsertService = transactionsUpsertService;
        this.paymentsService = paymentsService;
        this.reversalsService = reversalsService;
    }

    @Override
    public Response<Long> upsert(Long id, DisbursementRequest request) {
        Optional<LoanProductEntity> foundProduct = loanProductRepository.findById(request.loanProductId());
        if (foundProduct.isEmpty()) {
            return new Response<>(NOT_FOUND, "Loan product not found", null);
        }
        LoanProductEntity product = foundProduct.get();

        Optional<ReasonTypeEntity> foundReasonType = product.getReasonTypes().stream().filter(r -> r.getName().toLowerCase().contains("disbursement")).findAny();
        if (foundReasonType.isEmpty()) {
            return new Response<>(INTERNAL_SERVER_ERROR, "Loan product has not been properly configured", null);
        }

        Optional<CurrencyEntity> foundCurrency = currencyRepository.findById(request.currencyId());
        if (foundCurrency.isEmpty()) {
            return new Response<>(NOT_FOUND, "Currency not found", null);
        }
        CurrencyEntity currency = foundCurrency.get();

        ReasonTypeEntity reasonType = foundReasonType.get();
        Response<TransactionEntity> transactionResponse = createTransaction(request, reasonType, product);
        if (transactionResponse.responseCode().isError()) {
            return new Response<>(transactionResponse.responseCode(), transactionResponse.responseDesc(), null);
        }

        Response<String> sendResponse = paymentsService.send(formatCurrency(request.amountMinor(), currency), "", "");
        if (sendResponse.responseCode().isError()) {
            Response<Long> reversalResponse = reversalsService.reverse(transactionResponse.data());
            if (reversalResponse.responseCode().isError()) {
                throw new RuntimeException("Unable to reverse transaction: " + reversalResponse.responseDesc());
            }
            return new Response<>(sendResponse.responseCode(), sendResponse.responseDesc(), null);
        }
        LoanEntity loan = createLoan(request, transactionResponse.data(), product, currency);
        return new Response<>(CREATED, "Disbursement completed", loan.getId());
    }

    private Response<TransactionEntity> createTransaction(DisbursementRequest request, ReasonTypeEntity reasonType, LoanProductEntity product) {
        List<TransactionEntryRequest> entries = createTransactionEntries(request, reasonType, product);
        TransactionRequest transactionRequest = new TransactionRequest(request.idempotencyKey(), 0, request.customerId(), null, entries);
        Response<Long> response = transactionsUpsertService.upsert(null, transactionRequest);
        if (response.responseCode().isError()) {
            return new Response<>(response.responseCode(), response.responseDesc(), null);
        }
        TransactionEntity transaction = transactionRepository.findById(response.data()).orElseThrow();
        return new Response<>(CREATED, "Disbursement transaction created", transaction);
    }

    private List<TransactionEntryRequest> createTransactionEntries(DisbursementRequest request, ReasonTypeEntity reasonType, LoanProductEntity product) {
        List<AccountEntity> accounts = new ArrayList<>();
        for (TransactionTypeEntity transactionType : reasonType.getTransactionTypes()) {
            accounts.addAll(transactionType.getDebitAccounts());
            accounts.addAll(transactionType.getCreditAccounts());
        }

        long accessFee = calculateAccessFee(product.isAccessFeeChargeable(), product.getAccessFeePercentage(), request.amountMinor());
        List<TransactionEntryRequest> entries = new ArrayList<>();
        for (AccountEntity account : accounts) {
            String typeName = account.getAccountType().getName();
            if (typeName.equals("Loans Receivable")) {
                TransactionEntryRequest entry = new TransactionEntryRequest(account.getId(), request.amountMinor(), 0, request.currencyId());
                entries.add(entry);
            } else if (typeName.contains("Cash Account")) {
                TransactionEntryRequest entry = new TransactionEntryRequest(account.getId(), 0, request.amountMinor(), request.currencyId());
                entries.add(entry);
            } else if (typeName.contains("Loan Origination Fee Receivable")) {
                TransactionEntryRequest entry = new TransactionEntryRequest(account.getId(), accessFee, 0, request.currencyId());
                entries.add(entry);
            } else if (typeName.contains("Fee Income")) {
                TransactionEntryRequest entry = new TransactionEntryRequest(account.getId(), 0, accessFee, request.currencyId());
                entries.add(entry);
            }
        }
        return entries;
    }

    private LoanEntity createLoan(DisbursementRequest request, TransactionEntity transaction, LoanProductEntity product, CurrencyEntity currency) {
        long principal = request.amountMinor();
        long accessFee = calculateAccessFee(product.isAccessFeeChargeable(), product.getAccessFeePercentage(), principal);
        long totalAmount = principal + accessFee; // using gross disbursal

        LoanEntity entity = new LoanEntity();
        entity.setCustomer(transaction.getOppositeParty());
        entity.setPrincipalAmountMinor(principal);
        entity.setAccessFeeMinor(accessFee);
        entity.setTotalAmountMinor(totalAmount);
        entity.setCurrentBalance(totalAmount);
        entity.setCurrency(currency);
        entity.setDueDate(calculateDueDate(product.getTerm(), product.getTermUnit()));
        entity.setStatus(LoanStatus.ACTIVE);
        entity.addLoanTransaction(createLoanTransaction(entity, transaction));
        return loanRepository.save(entity);
    }

    private LoanTransactionEntity createLoanTransaction(LoanEntity loan, TransactionEntity transaction) {
        LoanTransactionEntity entity = new LoanTransactionEntity();
        entity.setLoan(loan);
        entity.setTransaction(transaction);
        entity.setLoanTransactionType("DISBURSEMENT");

        return entity;
    }

    private OffsetDateTime calculateDueDate(int loanTerm, TermUnit termUnit) {
        return switch (termUnit) {
            case D -> OffsetDateTime.now().plusDays(loanTerm);
            case W -> OffsetDateTime.now().plusWeeks(loanTerm);
            case M -> OffsetDateTime.now().plusMonths(loanTerm);
            case Y -> OffsetDateTime.now().plusYears(loanTerm);
        };
    }

    private long calculateAccessFee(boolean isCharged, BigDecimal accessFeePercentage, long principalMinor) {
        if (!isCharged) {
            return 0;
        }

        BigDecimal principal = new BigDecimal(principalMinor);
        BigDecimal fee = principal.multiply(accessFeePercentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        return fee.longValueExact();
    }

    private BigDecimal formatCurrency(long amountMinor, CurrencyEntity currency) {
        return new BigDecimal(amountMinor).divide(BigDecimal.valueOf(100), currency.getDecimalPlaces(), RoundingMode.HALF_UP);
    }
}
