package com.wilburomae.pezeshalms.accounts.dtos;

import jakarta.validation.constraints.NotBlank;

public record AccountTypeRequest(@NotBlank String name, @NotBlank String description, Long parentAccountTypeId) {
}
