package com.wilburomae.pezeshalms.transactions.services;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountEntity;
import com.wilburomae.pezeshalms.accounts.data.repositories.AccountRepository;
import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionTypeComponentEntity;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionTypeEntity;
import com.wilburomae.pezeshalms.transactions.data.repositories.TransactionTypeComponentRepository;
import com.wilburomae.pezeshalms.transactions.data.repositories.TransactionTypeRepository;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionTypeComponentRequest;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionTypeRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Service
public class TransactionTypesUpsertService implements UpsertService<TransactionTypeRequest> {

    private final TransactionTypeRepository transactionTypeRepository;
    private final TransactionTypeComponentRepository transactionTypeComponentRepository;
    private final AccountRepository accountRepository;

    public TransactionTypesUpsertService(TransactionTypeRepository transactionTypeRepository, TransactionTypeComponentRepository transactionTypeComponentRepository, AccountRepository accountRepository) {
        this.transactionTypeRepository = transactionTypeRepository;
        this.transactionTypeComponentRepository = transactionTypeComponentRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    @Override
    public Response<Long> upsert(Long id, TransactionTypeRequest request) {
        Response<TransactionTypeEntity> initResponse = initEntity(id, transactionTypeRepository, TransactionTypeEntity::new);
        if (initResponse.responseCode().isError()) {
            return new Response<>(NOT_FOUND, "Transaction type not found", null);
        }
        TransactionTypeEntity entity = initResponse.data();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setReversible(request.reversible());

        Response<Void> componentResponse = processComponents(entity, request.components());
        if (componentResponse.responseCode().isError()) {
            return new Response<>(componentResponse.responseCode(), componentResponse.responseDesc(), null);
        }

        entity = transactionTypeRepository.save(entity);
        return successResponse(id, "Transaction type", entity);
    }

    private Response<Void> processComponents(TransactionTypeEntity entity, List<TransactionTypeComponentRequest> components) {
        List<AccountEntity> accounts = accountRepository.findAll();

        for (TransactionTypeComponentRequest component : components) {
            Optional<AccountEntity> foundDebitAccount = accounts.stream().filter(a -> a.getId() == component.debitAccountId()).findAny();
            if (foundDebitAccount.isEmpty()) {
                return new Response<>(NOT_FOUND, "Debit account not found", null);
            }

            Optional<AccountEntity> foundCreditAccount = accounts.stream().filter(a -> a.getId() == component.creditAccountId()).findAny();
            if (foundCreditAccount.isEmpty()) {
                return new Response<>(NOT_FOUND, "Credit account not found", null);
            }

            TransactionTypeComponentEntity componentEntity;
            if (component.id() == null) {
                componentEntity = new TransactionTypeComponentEntity();
            } else {
                Optional<TransactionTypeComponentEntity> foundComponent = transactionTypeComponentRepository.findById(component.id());
                if (foundComponent.isEmpty()) {
                    return new Response<>(NOT_FOUND, "Transaction type component not found", null);
                }
                componentEntity = foundComponent.get();
            }

            componentEntity.setName(component.name());
            componentEntity.setDescription(component.description());
            componentEntity.setTransactionType(entity);
            componentEntity.setExecutionOrder(component.executionOrder());
            componentEntity.setPermission(null);
            componentEntity.setDebitAccount(foundDebitAccount.get());
            componentEntity.setCreditAccount(foundCreditAccount.get());

            entity.addTransactionTypeComponent(componentEntity);
        }

        return new Response<>(OK, "Transaction type components processed", null);
    }
}
