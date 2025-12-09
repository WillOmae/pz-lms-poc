package com.wilburomae.pezeshalms.products.dtos;

import java.math.BigDecimal;
import java.util.List;

public record LoanProductRequest(String name,
                                 String description,
                                 boolean interestChargeable,
                                 BigDecimal interestRate,
                                 boolean accessFeeChargeable,
                                 BigDecimal accessFeePercentage,
                                 int term,
                                 String interestTermUnit,
                                 String termUnit,
                                 int maxDisbursementsPerCustomer,
                                 boolean active,
                                 List<Long> partners,
                                 List<Long> reasonTypes) {
}
