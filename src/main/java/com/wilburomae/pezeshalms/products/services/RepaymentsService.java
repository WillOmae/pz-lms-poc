package com.wilburomae.pezeshalms.products.services;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountEntity;
import com.wilburomae.pezeshalms.accounts.data.entities.CurrencyEntity;
import com.wilburomae.pezeshalms.accounts.data.repositories.CurrencyRepository;
import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import com.wilburomae.pezeshalms.products.data.entities.LoanEntity;
import com.wilburomae.pezeshalms.products.data.entities.LoanProductEntity;
import com.wilburomae.pezeshalms.products.data.entities.LoanRepaymentScheduleEntity;
import com.wilburomae.pezeshalms.products.data.repositories.LoanProductRepository;
import com.wilburomae.pezeshalms.products.data.repositories.LoanRepository;
import com.wilburomae.pezeshalms.products.dtos.RepaymentRequest;
import com.wilburomae.pezeshalms.transactions.data.entities.ReasonTypeEntity;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionEntity;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionTypeEntity;
import com.wilburomae.pezeshalms.transactions.data.repositories.TransactionRepository;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionEntryRequest;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionRequest;
import com.wilburomae.pezeshalms.transactions.services.TransactionsUpsertService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
public class RepaymentsService implements UpsertService<RepaymentRequest> {

    private final LoanRepository loanRepository;
    private final LoanProductRepository loanProductRepository;
    private final TransactionRepository transactionRepository;
    private final CurrencyRepository currencyRepository;
    private final TransactionsUpsertService transactionsUpsertService;

    public RepaymentsService(LoanRepository loanRepository, LoanProductRepository loanProductRepository, TransactionRepository transactionRepository, CurrencyRepository currencyRepository, TransactionsUpsertService transactionsUpsertService) {
        this.loanRepository = loanRepository;
        this.loanProductRepository = loanProductRepository;
        this.transactionRepository = transactionRepository;
        this.currencyRepository = currencyRepository;
        this.transactionsUpsertService = transactionsUpsertService;
    }

    @Transactional
    @Override
    public Response<Long> upsert(Long id, RepaymentRequest request) {
        Optional<LoanProductEntity> foundProduct = loanProductRepository.findById(request.loanProductId());
        if (foundProduct.isEmpty()) {
            return new Response<>(NOT_FOUND, "Loan product not found", null);
        }
        LoanProductEntity product = foundProduct.get();

        Optional<LoanEntity> foundLoan = loanRepository.findById(id);
        if (foundLoan.isEmpty()) {
            return new Response<>(NOT_FOUND, "Loan not found", null);
        }
        LoanEntity loan = foundLoan.get();

        Optional<ReasonTypeEntity> foundReasonType = product.getReasonTypes().stream().filter(r -> r.getName().toLowerCase().contains("disbursement")).findAny();
        if (foundReasonType.isEmpty()) {
            return new Response<>(INTERNAL_SERVER_ERROR, "Loan product has not been properly configured", null);
        }

        Optional<CurrencyEntity> foundCurrency = currencyRepository.findById(request.currencyId());
        if (foundCurrency.isEmpty()) {
            return new Response<>(NOT_FOUND, "Currency not found", null);
        }

        long interest = calculateInterestPortion(product.isInterestChargeable(), product.getInterestRate(), request.amountMinor());
        long principal = request.amountMinor() - interest;
        ReasonTypeEntity reasonType = foundReasonType.get();
        Response<TransactionEntity> transactionResponse = createTransaction(request, reasonType, interest, principal);
        if (transactionResponse.responseCode().isError()) {
            return new Response<>(transactionResponse.responseCode(), transactionResponse.responseDesc(), null);
        }

        loan = updateLoanBalances(loan, transactionResponse.data(), interest, principal);
        return new Response<>(CREATED, "Repayment completed", loan.getId());
    }

    private Response<TransactionEntity> createTransaction(RepaymentRequest request, ReasonTypeEntity reasonType, long interest, long principal) {
        List<TransactionEntryRequest> entries = createTransactionEntries(request, reasonType, interest, principal);
        TransactionRequest transactionRequest = new TransactionRequest(request.idempotencyKey(), 0, request.customerId(), null, entries);
        Response<Long> response = transactionsUpsertService.upsert(null, transactionRequest);
        if (response.responseCode().isError()) {
            return new Response<>(response.responseCode(), response.responseDesc(), null);
        }
        TransactionEntity transaction = transactionRepository.findById(response.data()).orElseThrow();
        return new Response<>(CREATED, "Repayment transaction created", transaction);
    }

    private List<TransactionEntryRequest> createTransactionEntries(RepaymentRequest request, ReasonTypeEntity reasonType, long interest, long principal) {
        List<AccountEntity> accounts = new ArrayList<>();
        for (TransactionTypeEntity transactionType : reasonType.getTransactionTypes()) {
            accounts.addAll(transactionType.getDebitAccounts());
            accounts.addAll(transactionType.getCreditAccounts());
        }

        List<TransactionEntryRequest> entries = new ArrayList<>();
        for (AccountEntity account : accounts) {
            String typeName = account.getAccountType().getName();
            if (typeName.equals("Loans Receivable")) {
                TransactionEntryRequest entry = new TransactionEntryRequest(account.getId(), 0, principal, request.currencyId());
                entries.add(entry);
            } else if (typeName.contains("Cash Account")) {
                TransactionEntryRequest entry = new TransactionEntryRequest(account.getId(), request.amountMinor(), 0, request.currencyId());
                entries.add(entry);
            } else if (typeName.contains("Interest Income")) {
                TransactionEntryRequest entry = new TransactionEntryRequest(account.getId(), 0, interest, request.currencyId());
                entries.add(entry);
            }
        }
        return entries;
    }

    private long calculateInterestPortion(boolean isCharged, BigDecimal interestPercentage, long loanBalanceMinor) {
        if (!isCharged) {
            return 0;
        }
        // the interest calculation logic needs to be refined
        BigDecimal principal = new BigDecimal(loanBalanceMinor);
        BigDecimal interest = principal.multiply(interestPercentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        return interest.longValueExact();
    }

    private LoanEntity updateLoanBalances(LoanEntity loan, TransactionEntity transaction, long interest, long principal) {
        long currentBalance = loan.getCurrentBalance();
        long newBalance = currentBalance - principal - interest;
        LoanRepaymentScheduleEntity schedule = new LoanRepaymentScheduleEntity();
        schedule.setTransaction(transaction);
        schedule.setPrincipalPortionMinor(principal);
        schedule.setInterestPortionMinor(interest);
        schedule.setCurrentBalance(newBalance);

        loan.addRepaymentSchedule(schedule);
        loan.setCurrentBalance(newBalance);
        return loanRepository.save(loan);
    }
}
