package com.wilburomae.pezeshalms.accounts.dtos;

import com.wilburomae.pezeshalms.accounts.data.entities.CurrencyEntity;

public record Currency(long id,
                       String code,
                       String numericCode,
                       String name,
                       String symbol,
                       short decimalPlaces,
                       boolean isActive) {

    public static Currency from(CurrencyEntity entity) {
        return new Currency(entity.getId(), entity.getCode(), entity.getNumericCode(), entity.getName(), entity.getSymbol(), entity.getDecimalPlaces(), entity.isActive());
    }
}
