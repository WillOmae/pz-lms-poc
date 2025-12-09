package com.wilburomae.pezeshalms.transactions.dtos;

import java.util.List;

public record TransactionTypeRequest(String name,
                                     String description,
                                     boolean reversible,
                                     List<Long> debitAccounts,
                                     List<Long> creditAccounts) {
}
