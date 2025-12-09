package com.wilburomae.pezeshalms.transactions.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReasonTypeRequest(@NotBlank String name,
                                @NotBlank String description,
                                @NotEmpty List<@NotNull Long> transactionTypes) {
}
