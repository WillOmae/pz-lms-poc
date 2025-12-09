package com.wilburomae.pezeshalms.transactions.dtos;

import com.wilburomae.pezeshalms.common.dtos.IdName;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionEntity;

import java.util.List;

public record Transaction(long id,
                          String transactionReference,
                          IdName type,
                          IdName oppositeParty,
                          List<TransactionEntry> entries) {

    public static Transaction from(TransactionEntity entity) {
        IdName type = new IdName(entity.getTransactionType().getId(), entity.getTransactionType().getName());
        IdName oppositeParty = new IdName(entity.getOppositeParty().getId(), entity.getOppositeParty().getName());
        List<TransactionEntry> entries = entity.getTransactionEntries().stream().map(TransactionEntry::from).toList();
        return new Transaction(entity.getId(), entity.getTransactionReference(), type, oppositeParty, entries);
    }
}
