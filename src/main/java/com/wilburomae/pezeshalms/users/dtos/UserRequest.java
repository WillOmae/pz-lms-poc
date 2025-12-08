package com.wilburomae.pezeshalms.users.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record UserRequest(@NotBlank String name,
                          @NotBlank @Pattern(regexp = "^[A-Z]+$") String type,
                          @NotEmpty List<@NotNull Contact> contacts,
                          @NotEmpty List<@NotNull Identification> identifications,
                          @NotEmpty List<@NotNull Long> roleIds) {
}
