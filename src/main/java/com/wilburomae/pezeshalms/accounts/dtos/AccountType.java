package com.wilburomae.pezeshalms.accounts.dtos;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountTypeEntity;

public record AccountType(long id, String name, String description) {

    public static AccountType from(AccountTypeEntity entity) {
        return new AccountType(entity.getId(), entity.getName(), entity.getDescription());
    }
}
