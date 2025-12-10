package com.wilburomae.pezeshalms.products.dtos;

import com.wilburomae.pezeshalms.common.dtos.IdName;
import com.wilburomae.pezeshalms.products.data.entities.LoanEntity;

import java.time.OffsetDateTime;

public record Disbursement(long loanId,
                           long amountMinor,
                           long accessFee,
                           IdName currency,
                           String transactionReference,
                           OffsetDateTime dueDate,
                           String status) {

    public static Disbursement from(LoanEntity entity) {
        IdName currency = new IdName(entity.getCurrency().getId(), entity.getCurrency().getCode());
        return new Disbursement(
                entity.getId(),
                entity.getPrincipalAmountMinor(),
                entity.getAccessFeeMinor(),
                currency,
                null,
                entity.getDueDate(),
                entity.getStatus().name()
        );
    }
}
