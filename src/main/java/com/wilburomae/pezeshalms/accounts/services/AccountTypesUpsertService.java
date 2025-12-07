package com.wilburomae.pezeshalms.accounts.services;

import com.wilburomae.pezeshalms.accounts.dtos.AccountTypeRequest;
import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import org.springframework.stereotype.Service;

@Service
public class AccountTypesUpsertService implements UpsertService<AccountTypeRequest> {

    @Override
    public Response<Long> upsert(Long id, AccountTypeRequest request) {
        return null;
    }
}
