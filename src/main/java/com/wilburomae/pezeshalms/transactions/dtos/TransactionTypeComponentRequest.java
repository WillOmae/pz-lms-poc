package com.wilburomae.pezeshalms.transactions.dtos;

public record TransactionTypeComponentRequest(Long id,
                                              String name,
                                              String description,
                                              short executionOrder,
                                              long debitAccountId,
                                              long creditAccountId) {
}
