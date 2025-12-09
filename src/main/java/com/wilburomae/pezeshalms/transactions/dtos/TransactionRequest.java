package com.wilburomae.pezeshalms.transactions.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TransactionRequest(@NotBlank String idempotencyKey,
                                 long typeId,
                                 long oppositePartyId,
                                 @NotEmpty List<@NotNull TransactionEntryRequest> entries) {
}
