package com.wilburomae.pezeshalms.accounts.services;

import com.wilburomae.pezeshalms.accounts.dtos.CurrencyRequest;
import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import org.springframework.stereotype.Service;

@Service
public class CurrenciesUpsertService implements UpsertService<CurrencyRequest> {

    @Override
    public Response<Long> upsert(Long id, CurrencyRequest request) {
        return null;
    }
}
