package com.wilburomae.pezeshalms.accounts.dtos;

public record AccountBalanceRequest(long accountId,
                                    long currencyId,
                                    long balanceMinor,
                                    Long upperThresholdMinor,
                                    Long lowerThresholdMinor) {
}
