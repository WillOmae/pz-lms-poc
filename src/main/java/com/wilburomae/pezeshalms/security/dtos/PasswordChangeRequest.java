package com.wilburomae.pezeshalms.security.dtos;

import jakarta.validation.constraints.NotBlank;

public record PasswordChangeRequest(@NotBlank String contact, @NotBlank String newPassword) {
}