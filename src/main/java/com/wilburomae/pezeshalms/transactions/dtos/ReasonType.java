package com.wilburomae.pezeshalms.transactions.dtos;

import com.wilburomae.pezeshalms.common.dtos.IdName;
import com.wilburomae.pezeshalms.transactions.data.entities.ReasonTypeEntity;

import java.util.List;

public record ReasonType(long id, String name, String description, List<IdName> transactionTypes) {

    public static ReasonType from(ReasonTypeEntity entity) {
        List<IdName> transactionTypes = entity.getTransactionTypes().stream().map(account -> new IdName(account.getId(), account.getName())).toList();

        return new ReasonType(entity.getId(), entity.getName(), entity.getDescription(), transactionTypes);
    }
}
