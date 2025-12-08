package com.wilburomae.pezeshalms.security;

import jakarta.validation.constraints.NotBlank;

public record PasswordChangeRequest(@NotBlank String contact, @NotBlank String newPassword) {
}