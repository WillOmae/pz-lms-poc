package com.wilburomae.pezeshalms.accounts.dtos;

public record AccountBalanceRequest(Long id,
                                    long currencyId,
                                    long balanceMinor,
                                    Long upperThresholdMinor,
                                    Long lowerThresholdMinor) {
}
