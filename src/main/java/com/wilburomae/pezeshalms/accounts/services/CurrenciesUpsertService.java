package com.wilburomae.pezeshalms.accounts.services;

import com.wilburomae.pezeshalms.accounts.data.entities.CurrencyEntity;
import com.wilburomae.pezeshalms.accounts.data.repositories.CurrencyRepository;
import com.wilburomae.pezeshalms.accounts.dtos.CurrencyRequest;
import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CurrenciesUpsertService implements UpsertService<CurrencyRequest> {

    private final CurrencyRepository currencyRepository;

    public CurrenciesUpsertService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public Response<Long> upsert(Long id, CurrencyRequest request) {
        Response<CurrencyEntity> initResponse = initEntity(id, currencyRepository, CurrencyEntity::new);
        if (initResponse.responseCode().isError()) {
            return new Response<>(NOT_FOUND, "Currency not found", null);
        }
        CurrencyEntity entity = initResponse.data();
        entity.setCode(request.code());
        entity.setNumericCode(request.numericCode());
        entity.setName(request.name());
        entity.setSymbol(request.symbol());
        entity.setDecimalPlaces(request.decimalPlaces());
        entity.setActive(request.isActive());

        currencyRepository.save(entity);
        return successResponse(id, "Currency", entity);
    }
}
