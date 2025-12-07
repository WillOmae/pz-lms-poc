package com.wilburomae.pezeshalms.accounts.services;

import com.wilburomae.pezeshalms.accounts.dtos.AccountBalanceRequest;
import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import org.springframework.stereotype.Service;

@Service
public class AccountBalancesUpsertService implements UpsertService<AccountBalanceRequest> {

    @Override
    public Response<Long> upsert(Long id, AccountBalanceRequest request) {
        return null;
    }
}
