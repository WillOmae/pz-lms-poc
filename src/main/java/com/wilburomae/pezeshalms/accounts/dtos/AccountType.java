package com.wilburomae.pezeshalms.accounts.dtos;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountTypeEntity;

public record AccountType(long id, String name, String description, Long parentTypeId, String parentTypeName,
                          String parentTypeDescription) {

    public static AccountType from(AccountTypeEntity entity) {
        AccountTypeEntity parent = entity.getParentAccountType();
        if (parent == null) {
            return new AccountType(entity.getId(), entity.getName(), entity.getDescription(), null, null, null);
        } else {
            return new AccountType(entity.getId(), entity.getName(), entity.getDescription(), parent.getId(), parent.getName(), parent.getDescription());
        }
    }
}
