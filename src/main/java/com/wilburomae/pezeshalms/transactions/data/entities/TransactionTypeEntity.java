package com.wilburomae.pezeshalms.transactions.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import com.wilburomae.pezeshalms.products.data.entities.ProductTransactionTypeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
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
}