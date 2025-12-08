package com.wilburomae.pezeshalms.accounts.services;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountStatusEntity;
import com.wilburomae.pezeshalms.accounts.data.repositories.AccountStatusRepository;
import com.wilburomae.pezeshalms.accounts.dtos.AccountStatusRequest;
import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class AccountStatusesUpsertService implements UpsertService<AccountStatusRequest> {

    private final AccountStatusRepository accountStatusRepository;

    public AccountStatusesUpsertService(AccountStatusRepository accountStatusRepository) {
        this.accountStatusRepository = accountStatusRepository;
    }

    @Transactional
    @Override
    public Response<Long> upsert(Long id, AccountStatusRequest request) {
        Response<AccountStatusEntity> initResponse = initEntity(id, accountStatusRepository, AccountStatusEntity::new);
        if (initResponse.responseCode().isError()) {
            return new Response<>(NOT_FOUND, "Account status not found", null);
        }
        AccountStatusEntity entity = initResponse.data();
        entity.setName(request.name());
        entity.setDescription(request.description());

        entity = accountStatusRepository.save(entity);
        return successResponse(id, "Account status", entity);
    }
}
