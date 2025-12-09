package com.wilburomae.pezeshalms.transactions.dtos;

public record TransactionEntryRequest(long accountId, long debitAmountMinor, long creditAmountMinor, long currencyId) {
}
