package com.wilburomae.pezeshalms.products.dtos;

import com.wilburomae.pezeshalms.common.dtos.IdName;
import com.wilburomae.pezeshalms.products.data.entities.LoanEntity;

import java.time.OffsetDateTime;

public record Repayment(long loanId,
                        long loanAmountMinor,
                        long balance,
                        IdName currency,
                        OffsetDateTime dueDate,
                        String status) {

    public static Repayment from(LoanEntity entity) {
        IdName currency = new IdName(entity.getCurrency().getId(), entity.getCurrency().getCode());
        return new Repayment(
                entity.getId(),
                entity.getPrincipalAmountMinor(),
                entity.getCurrentBalance(),
                currency,
                entity.getDueDate(),
                entity.getStatus().name()
        );
    }
}
