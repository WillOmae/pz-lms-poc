package com.wilburomae.pezeshalms.products.dtos;

import com.wilburomae.pezeshalms.common.dtos.IdName;
import com.wilburomae.pezeshalms.products.data.entities.LoanEntity;

import java.time.OffsetDateTime;

public record WriteOff(long loanId,
                       long loanAmountMinor,
                       long writeOffAmountMinor,
                       IdName currency,
                       OffsetDateTime dueDate,
                       String status) {

    public static WriteOff from(LoanEntity entity) {
        IdName currency = new IdName(entity.getCurrency().getId(), entity.getCurrency().getCode());
        return new WriteOff(
                entity.getId(),
                entity.getPrincipalAmountMinor(),
                entity.getCurrentBalance(),
                currency,
                entity.getDueDate(),
                entity.getStatus().name()
        );
    }
}
