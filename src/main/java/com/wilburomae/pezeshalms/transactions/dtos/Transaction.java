package com.wilburomae.pezeshalms.transactions.dtos;

import com.wilburomae.pezeshalms.transactions.data.entities.TransactionEntity;
import com.wilburomae.pezeshalms.users.dtos.User;

import java.util.List;

public record Transaction(long id,
                          String transactionReference,
                          TransactionType type,
                          User oppositeParty,
                          List<TransactionEntry> entries) {

    public static Transaction from(TransactionEntity entity) {
        TransactionType type = TransactionType.from(entity.getTransactionType());
        User oppositeParty = User.from(entity.getOppositeParty());
        List<TransactionEntry> entries = entity.getTransactionEntries().stream().map(TransactionEntry::from).toList();
        return new Transaction(entity.getId(), entity.getTransactionReference(), type, oppositeParty, entries);
    }
}
