package com.wilburomae.pezeshalms.transactions.dtos;

import com.wilburomae.pezeshalms.accounts.dtos.Account;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionTypeComponentEntity;

public record TransactionTypeComponent(long id,
                                       String name,
                                       String description,
                                       short executionOrder,
                                       Account debitAccount,
                                       Account creditAccount) {

    public static TransactionTypeComponent from(TransactionTypeComponentEntity entity) {
        return new TransactionTypeComponent(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getExecutionOrder(),
                Account.from(entity.getDebitAccount()),
                Account.from(entity.getCreditAccount())
        );
    }
}
