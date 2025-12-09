package com.wilburomae.pezeshalms.products.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record LoanProductRequest(@NotBlank String name,
                                 @NotBlank String description,
                                 boolean interestChargeable,
                                 BigDecimal interestRate,
                                 boolean accessFeeChargeable,
                                 BigDecimal accessFeePercentage,
                                 int term,
                                 String interestTermUnit,
                                 String termUnit,
                                 int maxDisbursementsPerCustomer,
                                 boolean active,
                                 @NotEmpty List<@NotNull Long> partners,
                                 @NotEmpty List<@NotNull Long> reasonTypes) {
}
