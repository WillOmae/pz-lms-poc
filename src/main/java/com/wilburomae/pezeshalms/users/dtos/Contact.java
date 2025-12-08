package com.wilburomae.pezeshalms.users.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record Contact(@NotBlank String contact, @NotBlank @Pattern(regexp = "^PHONE|EMAIL$") String contactType,
                      boolean isPrimary) {
}
