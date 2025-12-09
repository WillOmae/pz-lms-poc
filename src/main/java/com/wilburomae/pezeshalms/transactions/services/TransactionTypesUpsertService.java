package com.wilburomae.pezeshalms.transactions.services;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountEntity;
import com.wilburomae.pezeshalms.accounts.data.repositories.AccountRepository;
import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionTypeEntity;
import com.wilburomae.pezeshalms.transactions.data.repositories.TransactionTypeRepository;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionTypeRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class TransactionTypesUpsertService implements UpsertService<TransactionTypeRequest> {

    private final TransactionTypeRepository transactionTypeRepository;
    private final AccountRepository accountRepository;

    public TransactionTypesUpsertService(TransactionTypeRepository transactionTypeRepository, AccountRepository accountRepository) {
        this.transactionTypeRepository = transactionTypeRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    @Override
    public Response<Long> upsert(Long id, TransactionTypeRequest request) {
        Map<Long, AccountEntity> accounts = new HashMap<>();
        accountRepository.findAll().forEach(account -> accounts.put(account.getId(), account));

        Response<TransactionTypeEntity> initResponse = initEntity(id, transactionTypeRepository, TransactionTypeEntity::new);
        if (initResponse.responseCode().isError()) {
            return new Response<>(NOT_FOUND, "Transaction type not found", null);
        }

        TransactionTypeEntity entity = initResponse.data();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setReversible(request.reversible());

        for (long accountId : request.debitAccounts()) {
            AccountEntity account = accounts.get(accountId);
            if (account == null) {
                return new Response<>(NOT_FOUND, "Debit account not found", null);
            }
            entity.addDebitAccount(account);
        }

        for (long accountId : request.creditAccounts()) {
            AccountEntity account = accounts.get(accountId);
            if (account == null) {
                return new Response<>(NOT_FOUND, "Credit account not found", null);
            }
            entity.addCreditAccount(account);
        }

        entity = transactionTypeRepository.save(entity);
        return successResponse(id, "Transaction type", entity);
    }
}
