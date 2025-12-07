package com.wilburomae.pezeshalms.accounts.dtos;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountEntity;

import java.util.List;

public record Account(long id,
                      AccountType type,
                      boolean overdrawable,
                      AccountStatus status,
                      List<AccountBalance> balances) {

    public static Account from(AccountEntity entity) {
        AccountType type = AccountType.from(entity.getAccountType());
        AccountStatus status = AccountStatus.from(entity.getAccountStatus());
        List<AccountBalance> balances = entity.getAccountBalances().stream().map(AccountBalance::from).toList();
        return new Account(entity.getId(), type, entity.isOverdrawable(), status, balances);
    }
}
