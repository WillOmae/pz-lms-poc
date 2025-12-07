package com.wilburomae.pezeshalms.accounts.dtos;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountStatusEntity;

public record AccountStatus(long id, String name, String description) {

    public static AccountStatus from(AccountStatusEntity entity) {
        return new AccountStatus(entity.getId(), entity.getName(), entity.getDescription());
    }
}
