package com.wilburomae.pezeshalms.transactions.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import com.wilburomae.pezeshalms.products.data.entities.LoanProductEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "reason_types", schema = "lms")
public class ReasonTypeEntity extends IdAuditableEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "reason_type_transaction_types", schema = "lms", joinColumns = @JoinColumn(name = "reason_type_id"), inverseJoinColumns = @JoinColumn(name = "transaction_type_id"))
    private Set<TransactionTypeEntity> transactionTypes = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "reasonTypes")
    private Set<LoanProductEntity> loanProducts = new LinkedHashSet<>();

    public void addTransactionType(TransactionTypeEntity entity) {
        Optional<TransactionTypeEntity> existing = transactionTypes.stream()
                .filter(t -> t.getDateCreated() != null && t.getId() == entity.getId())
                .findAny();

        if (existing.isPresent()) return;

        transactionTypes.add(entity);
        entity.getReasonTypes().add(this);
    }
}