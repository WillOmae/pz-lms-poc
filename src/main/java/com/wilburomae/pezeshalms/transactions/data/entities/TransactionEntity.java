package com.wilburomae.pezeshalms.transactions.data.entities;

import com.wilburomae.pezeshalms.accounts.data.entities.CurrencyEntity;
import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "transactions", schema = "lms")
public class TransactionEntity extends IdAuditableEntity {

    @Column(name = "transaction_reference")
    private String transactionReference;

    @Column(name = "amount_minor")
    private long amountMinor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency")
    private CurrencyEntity currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_type_id")
    private TransactionTypeEntity transactionType;

    @OneToMany(mappedBy = "transaction")
    private Set<TransactionEntryEntity> transactionEntries = new LinkedHashSet<>();
}