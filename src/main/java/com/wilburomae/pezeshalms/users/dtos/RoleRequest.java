package com.wilburomae.pezeshalms.users.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RoleRequest(@NotBlank String name,
                          @NotBlank String description,
                          @NotEmpty List<@NotNull Long> permissionIds) {
}
