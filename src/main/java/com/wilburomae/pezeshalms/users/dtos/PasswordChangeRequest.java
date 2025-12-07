package com.wilburomae.pezeshalms.users.dtos;

import jakarta.validation.constraints.NotBlank;

public record PasswordChangeRequest(@NotBlank String contact, @NotBlank String newPassword) {
}