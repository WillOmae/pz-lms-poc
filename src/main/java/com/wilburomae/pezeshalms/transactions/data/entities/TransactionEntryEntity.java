package com.wilburomae.pezeshalms.transactions.data.entities;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountEntity;
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

    @Column(name = "debit_amount_minor")
    private long debitAmountMinor;

    @Column(name = "credit_amount_minor")
    private long creditAmountMinor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency")
    private CurrencyEntity currency;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @Column(name = "account_balance_minor")
    private long accountBalanceMinor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private TransactionEntity transaction;
}