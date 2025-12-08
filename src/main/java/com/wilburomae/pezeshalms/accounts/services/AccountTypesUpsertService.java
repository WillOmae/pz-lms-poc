package com.wilburomae.pezeshalms.accounts.services;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountTypeEntity;
import com.wilburomae.pezeshalms.accounts.data.repositories.AccountTypeRepository;
import com.wilburomae.pezeshalms.accounts.dtos.AccountTypeRequest;
import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class AccountTypesUpsertService implements UpsertService<AccountTypeRequest> {

    private final AccountTypeRepository accountTypeRepository;

    public AccountTypesUpsertService(AccountTypeRepository accountTypeRepository) {
        this.accountTypeRepository = accountTypeRepository;
    }

    @Transactional
    @Override
    public Response<Long> upsert(Long id, AccountTypeRequest request) {
        Response<AccountTypeEntity> initResponse = initEntity(id, accountTypeRepository, AccountTypeEntity::new);
        if (initResponse.responseCode().isError()) {
            return new Response<>(NOT_FOUND, "Account type not found", null);
        }
        AccountTypeEntity entity = initResponse.data();
        entity.setName(request.name());
        entity.setDescription(request.description());

        if (request.parentAccountTypeId() != null) {
            Optional<AccountTypeEntity> foundParent = accountTypeRepository.findById(request.parentAccountTypeId());
            if (foundParent.isEmpty()) {
                return new Response<>(NOT_FOUND, "Parent account type not found", null);
            }
            AccountTypeEntity parentAccountType = foundParent.get();
            parentAccountType.addAccountType(entity);
        }
        entity = accountTypeRepository.save(entity);

        return successResponse(id, "Account type", entity);
    }
}
