package com.wilburomae.pezeshalms.products.dtos;

public record RepaymentRequest(String idempotencyKey,
                               long loanProductId,
                               long loanId,
                               long amountMinor,
                               long currencyId,
                               long customerId) {
}
