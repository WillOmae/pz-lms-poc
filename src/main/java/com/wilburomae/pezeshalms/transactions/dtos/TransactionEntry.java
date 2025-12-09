package com.wilburomae.pezeshalms.transactions.dtos;

import com.wilburomae.pezeshalms.common.dtos.IdName;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionEntryEntity;

public record TransactionEntry(long id,
                               String transactionReference,
                               IdName account,
                               long debitAmountMinor,
                               long creditAmountMinor,
                               IdName currency) {

    public static TransactionEntry from(TransactionEntryEntity entity) {
        IdName account = new IdName(entity.getAccount().getId(), entity.getAccount().getName());
        IdName currency = new IdName(entity.getCurrency().getId(), entity.getCurrency().getName());
        return new TransactionEntry(entity.getId(), entity.getTransactionReference(), account, entity.getDebitAmountMinor(), entity.getCreditAmountMinor(), currency);
    }
}
