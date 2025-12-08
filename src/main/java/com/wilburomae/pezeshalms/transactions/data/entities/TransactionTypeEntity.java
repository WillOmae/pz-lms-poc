package com.wilburomae.pezeshalms.transactions.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import com.wilburomae.pezeshalms.products.data.entities.ProductTransactionTypeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "transaction_types", schema = "lms")
public class TransactionTypeEntity extends IdAuditableEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "reversible")
    private boolean reversible;

    @OneToMany(mappedBy = "transactionType")
    private Set<ProductTransactionTypeEntity> productTransactionTypes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "transactionType", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<TransactionTypeComponentEntity> transactionTypeComponents = new LinkedHashSet<>();

    @OneToMany(mappedBy = "transactionType")
    private Set<TransactionEntity> transactions = new LinkedHashSet<>();

    public void addTransactionTypeComponent(TransactionTypeComponentEntity entity) {
        Optional<TransactionTypeComponentEntity> existing = transactionTypeComponents.stream()
                .filter(t -> t.getDateCreated() != null && t.getId() == entity.getId())
                .findAny();

        if (existing.isPresent()) {
            TransactionTypeComponentEntity component = existing.get();
            component.setName(entity.getName());
            component.setDescription(entity.getDescription());
            component.setTransactionType(entity.getTransactionType());
            component.setExecutionOrder(entity.getExecutionOrder());
            component.setPermission(entity.getPermission());
            component.setDebitAccount(entity.getDebitAccount());
            component.setCreditAccount(entity.getCreditAccount());
        } else {
            transactionTypeComponents.add(entity);
            entity.setTransactionType(this);
        }
    }
}