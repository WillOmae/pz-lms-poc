package com.wilburomae.pezeshalms.accounts.dtos;

import jakarta.validation.constraints.NotBlank;

public record AccountStatusRequest(@NotBlank String name, @NotBlank String description) {
}
