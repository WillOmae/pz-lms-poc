package com.wilburomae.pezeshalms.accounts.dtos;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountEntity;
import com.wilburomae.pezeshalms.common.dtos.IdName;

import java.util.List;

public record Account(long id,
                      String name,
                      IdName type,
                      boolean overdrawable,
                      IdName status,
                      List<AccountBalance> balances) {

    public static Account from(AccountEntity entity) {
        IdName type = new IdName(entity.getAccountType().getId(), entity.getAccountType().getName());
        IdName status = new IdName(entity.getAccountStatus().getId(), entity.getAccountStatus().getName());
        List<AccountBalance> balances = entity.getAccountBalances().stream().map(AccountBalance::from).toList();
        return new Account(entity.getId(), entity.getName(), type, entity.isOverdrawable(), status, balances);
    }
}
