package com.wilburomae.pezeshalms.products.dtos;

public record DisbursementRequest(String idempotencyKey, long loanProductId, long amountMinor, long currencyId,
                                  long customerId) {
}
