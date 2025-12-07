package com.wilburomae.pezeshalms.accounts.services;

import com.wilburomae.pezeshalms.accounts.dtos.AccountStatusRequest;
import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import org.springframework.stereotype.Service;

@Service
public class AccountStatusesUpsertService implements UpsertService<AccountStatusRequest> {

    @Override
    public Response<Long> upsert(Long id, AccountStatusRequest request) {
        return null;
    }
}
