package com.wilburomae.pezeshalms.accounts.services;

import com.wilburomae.pezeshalms.accounts.dtos.PartnerAccountRequest;
import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import org.springframework.stereotype.Service;

@Service
public class PartnerAccountsUpsertService implements UpsertService<PartnerAccountRequest> {

    @Override
    public Response<Long> upsert(Long id, PartnerAccountRequest request) {
        return null;
    }
}
