package com.wilburomae.pezeshalms.transactions.data.entities;

import com.wilburomae.pezeshalms.accounts.data.entities.CurrencyEntity;
import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transaction_entries", schema = "lms")
public class TransactionEntryEntity extends IdAuditableEntity {

    @Column(name = "transaction_reference")
    private String transactionReference;

    @Column(name = "amount_minor")
    private long amountMinor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency")
    private CurrencyEntity currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_type_component_id")
    private TransactionTypeComponentEntity transactionTypeComponent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private TransactionEntity transaction;
}