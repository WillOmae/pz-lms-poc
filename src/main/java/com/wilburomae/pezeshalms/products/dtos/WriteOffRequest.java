package com.wilburomae.pezeshalms.products.dtos;

public record WriteOffRequest(String idempotencyKey, long loanProductId, long loanId, long currencyId) {
}
