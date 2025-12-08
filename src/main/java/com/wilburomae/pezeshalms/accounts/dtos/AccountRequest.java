package com.wilburomae.pezeshalms.accounts.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AccountRequest(Long id,
                             long typeId,
                             boolean overdrawable,
                             long statusId,
                             @NotEmpty List<@NotNull AccountBalanceRequest> balances) {
}
