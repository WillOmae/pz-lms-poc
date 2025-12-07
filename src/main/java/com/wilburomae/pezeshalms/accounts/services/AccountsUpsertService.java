package com.wilburomae.pezeshalms.accounts.services;

import com.wilburomae.pezeshalms.accounts.dtos.AccountRequest;
import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import org.springframework.stereotype.Service;

@Service
public class AccountsUpsertService implements UpsertService<AccountRequest> {

    @Override
    public Response<Long> upsert(Long id, AccountRequest request) {
        return null;
    }
}
