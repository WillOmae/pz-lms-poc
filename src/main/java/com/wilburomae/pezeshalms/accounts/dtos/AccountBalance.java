package com.wilburomae.pezeshalms.accounts.dtos;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountBalanceEntity;

public record AccountBalance(long id,
                             Currency currency,
                             long balanceMinor,
                             Long upperThresholdMinor,
                             Long lowerThresholdMinor) {

    public static AccountBalance from(AccountBalanceEntity entity) {
        Currency currency = Currency.from(entity.getCurrency());
        return new AccountBalance(entity.getId(), currency, entity.getBalanceMinor(), entity.getUpperThresholdMinor(), entity.getLowerThresholdMinor());
    }
}
