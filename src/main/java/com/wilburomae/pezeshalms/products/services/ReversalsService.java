package com.wilburomae.pezeshalms.products.services;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionEntity;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionEntryEntity;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionTypeEntity;
import com.wilburomae.pezeshalms.transactions.data.repositories.TransactionTypeRepository;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionEntryRequest;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionRequest;
import com.wilburomae.pezeshalms.transactions.services.TransactionsUpsertService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
public class ReversalsService {

    private final TransactionTypeRepository transactionTypeRepository;
    private final TransactionsUpsertService transactionsUpsertService;

    public ReversalsService(TransactionTypeRepository transactionTypeRepository, TransactionsUpsertService transactionsUpsertService) {
        this.transactionTypeRepository = transactionTypeRepository;
        this.transactionsUpsertService = transactionsUpsertService;
    }

    @Transactional
    public Response<Long> reverse(TransactionEntity transaction) {
        Optional<TransactionTypeEntity> loanReversal = transactionTypeRepository.findByName("Loan reversal");
        if (loanReversal.isEmpty()) {
            return new Response<>(INTERNAL_SERVER_ERROR, "Loan reversal transaction type not found", null);
        }
        TransactionTypeEntity loanReversalType = loanReversal.get();

        List<TransactionEntryRequest> entries = new ArrayList<>();
        for (TransactionEntryEntity entry : transaction.getTransactionEntries()) {
            TransactionEntryRequest reversedEntry = new TransactionEntryRequest(
                    // swap debit and credit amounts
                    entry.getId(), entry.getCreditAmountMinor(), entry.getDebitAmountMinor(), entry.getCurrency().getId()
            );
            entries.add(reversedEntry);
        }

        String idempotencyKey = UUID.randomUUID().toString();
        TransactionRequest reversalRequest = new TransactionRequest(
                idempotencyKey, loanReversalType.getId(), transaction.getOppositeParty().getId(), transaction.getId(), entries
        );
        return transactionsUpsertService.upsert(null, reversalRequest);
    }
}
