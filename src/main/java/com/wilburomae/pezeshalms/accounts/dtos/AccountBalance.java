package com.wilburomae.pezeshalms.accounts.dtos;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountBalanceEntity;
import com.wilburomae.pezeshalms.common.dtos.IdName;

public record AccountBalance(long id,
                             IdName currency,
                             long balanceMinor,
                             Long upperThresholdMinor,
                             Long lowerThresholdMinor) {

    public static AccountBalance from(AccountBalanceEntity entity) {
        IdName currency = new IdName(entity.getCurrency().getId(), entity.getCurrency().getCode());
        return new AccountBalance(entity.getId(), currency, entity.getBalanceMinor(), entity.getUpperThresholdMinor(), entity.getLowerThresholdMinor());
    }
}
