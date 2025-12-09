package com.wilburomae.pezeshalms.products.dtos;

import com.wilburomae.pezeshalms.common.dtos.IdName;
import com.wilburomae.pezeshalms.products.data.entities.LoanProductEntity;

import java.math.BigDecimal;
import java.util.List;

public record LoanProduct(long id,
                          String name,
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
                          List<IdName> partners,
                          List<IdName> reasonTypes) {

    public static LoanProduct from(LoanProductEntity entity) {
        List<IdName> partners = entity.getPartners().stream().map(partner -> new IdName(partner.getId(), partner.getName())).toList();
        List<IdName> reasonTypes = entity.getReasonTypes().stream().map(reasonType -> new IdName(reasonType.getId(), reasonType.getName())).toList();
        return new LoanProduct(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.isInterestChargeable(),
                entity.getInterestRate(),
                entity.isAccessFeeChargeable(),
                entity.getAccessFeePercentage(),
                entity.getTerm(),
                entity.getInterestTermUnit().name(),
                entity.getTermUnit().name(),
                entity.getMaxDisbursementsPerCustomer(),
                entity.isActive(),
                partners,
                reasonTypes
        );
    }
}
