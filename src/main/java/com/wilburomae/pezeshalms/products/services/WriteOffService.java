package com.wilburomae.pezeshalms.products.services;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountEntity;
import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import com.wilburomae.pezeshalms.products.data.entities.LoanEntity;
import com.wilburomae.pezeshalms.products.data.entities.LoanProductEntity;
import com.wilburomae.pezeshalms.products.data.entities.LoanRepaymentScheduleEntity;
import com.wilburomae.pezeshalms.products.data.entities.LoanStatus;
import com.wilburomae.pezeshalms.products.data.repositories.LoanProductRepository;
import com.wilburomae.pezeshalms.products.data.repositories.LoanRepository;
import com.wilburomae.pezeshalms.products.dtos.WriteOffRequest;
import com.wilburomae.pezeshalms.transactions.data.entities.ReasonTypeEntity;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionEntity;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionTypeEntity;
import com.wilburomae.pezeshalms.transactions.data.repositories.TransactionRepository;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionEntryRequest;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionRequest;
import com.wilburomae.pezeshalms.transactions.services.TransactionsUpsertService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
public class WriteOffService implements UpsertService<WriteOffRequest> {

    private final LoanRepository loanRepository;
    private final LoanProductRepository loanProductRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionsUpsertService transactionsUpsertService;

    public WriteOffService(LoanRepository loanRepository, LoanProductRepository loanProductRepository, TransactionRepository transactionRepository, TransactionsUpsertService transactionsUpsertService) {
        this.loanRepository = loanRepository;
        this.loanProductRepository = loanProductRepository;
        this.transactionRepository = transactionRepository;
        this.transactionsUpsertService = transactionsUpsertService;
    }

    @Transactional
    @Override
    public Response<Long> upsert(Long id, WriteOffRequest request) {
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

        long loanBalanceMinor = loan.getCurrentBalance();
        ReasonTypeEntity reasonType = foundReasonType.get();
        Response<TransactionEntity> transactionResponse = createTransaction(request, reasonType, loan.getCustomer().getId(), loanBalanceMinor);
        if (transactionResponse.responseCode().isError()) {
            return new Response<>(transactionResponse.responseCode(), transactionResponse.responseDesc(), null);
        }

        loan = updateLoanBalances(loan, transactionResponse.data());
        return new Response<>(CREATED, "Repayment completed", loan.getId());
    }

    private Response<TransactionEntity> createTransaction(WriteOffRequest request, ReasonTypeEntity reasonType, long customerId, long loanBalanceMinor) {
        List<TransactionEntryRequest> entries = createTransactionEntries(request, reasonType, loanBalanceMinor);
        TransactionRequest transactionRequest = new TransactionRequest(request.idempotencyKey(), 0, customerId, null, entries);
        Response<Long> response = transactionsUpsertService.upsert(null, transactionRequest);
        if (response.responseCode().isError()) {
            return new Response<>(response.responseCode(), response.responseDesc(), null);
        }
        TransactionEntity transaction = transactionRepository.findById(response.data()).orElseThrow();
        return new Response<>(CREATED, "Write off transaction created", transaction);
    }

    private List<TransactionEntryRequest> createTransactionEntries(WriteOffRequest request, ReasonTypeEntity reasonType, long loanBalanceMinor) {
        List<AccountEntity> accounts = new ArrayList<>();
        for (TransactionTypeEntity transactionType : reasonType.getTransactionTypes()) {
            accounts.addAll(transactionType.getDebitAccounts());
            accounts.addAll(transactionType.getCreditAccounts());
        }

        List<TransactionEntryRequest> entries = new ArrayList<>();
        for (AccountEntity account : accounts) {
            String typeName = account.getAccountType().getName();
            if (typeName.equals("Loans Receivable")) {
                TransactionEntryRequest entry = new TransactionEntryRequest(account.getId(), 0, loanBalanceMinor, request.currencyId());
                entries.add(entry);
            } else if (typeName.contains("Bad Debt Expense")) {
                TransactionEntryRequest entry = new TransactionEntryRequest(account.getId(), loanBalanceMinor, 0, request.currencyId());
                entries.add(entry);
            }
        }
        return entries;
    }

    private LoanEntity updateLoanBalances(LoanEntity loan, TransactionEntity transaction) {
        long newBalance = loan.getCurrentBalance();
        LoanRepaymentScheduleEntity schedule = new LoanRepaymentScheduleEntity();
        schedule.setTransaction(transaction);
        schedule.setPrincipalPortionMinor(0);
        schedule.setInterestPortionMinor(0);
        schedule.setCurrentBalance(newBalance);

        loan.setStatus(LoanStatus.WRITTEN_OFF);
        loan.addRepaymentSchedule(schedule);
        loan.setCurrentBalance(newBalance);
        return loanRepository.save(loan);
    }
}
