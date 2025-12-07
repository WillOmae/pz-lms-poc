package com.wilburomae.pezeshalms.accounts.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
}