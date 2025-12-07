package com.wilburomae.pezeshalms.accounts.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CurrencyRequest(@NotBlank @Pattern(regexp = "^[A-Z]{3}$") String code,
                              @NotBlank @Pattern(regexp = "^[0-9]{3}$") String numericCode,
                              @NotBlank @Size(max = 100) String name,
                              String symbol,
                              short decimalPlaces,
                              boolean isActive) {
}
