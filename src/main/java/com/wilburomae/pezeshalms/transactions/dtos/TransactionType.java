package com.wilburomae.pezeshalms.transactions.dtos;

import com.wilburomae.pezeshalms.common.dtos.IdName;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionTypeEntity;

import java.util.List;

public record TransactionType(long id,
                              String name,
                              String description,
                              Boolean reversible,
                              List<IdName> debitAccounts,
                              List<IdName> creditAccounts) {

    public static TransactionType from(TransactionTypeEntity entity) {
        List<IdName> debitAccounts = entity.getDebitAccounts().stream().map(account -> new IdName(account.getId(), account.getName())).toList();
        List<IdName> creditAccounts = entity.getDebitAccounts().stream().map(account -> new IdName(account.getId(), account.getName())).toList();

        return new TransactionType(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.isReversible(),
                debitAccounts,
                creditAccounts
        );
    }
}
