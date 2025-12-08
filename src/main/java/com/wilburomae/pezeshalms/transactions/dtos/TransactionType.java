package com.wilburomae.pezeshalms.transactions.dtos;

import com.wilburomae.pezeshalms.transactions.data.entities.TransactionTypeEntity;

import java.util.List;

public record TransactionType(long id,
                              String name,
                              String description,
                              boolean reversible,
                              List<TransactionTypeComponent> components) {

    public static TransactionType from(TransactionTypeEntity entity) {
        List<TransactionTypeComponent> components = entity.getTransactionTypeComponents().stream().map(TransactionTypeComponent::from).toList();

        return new TransactionType(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.isReversible(),
                components
        );
    }
}
