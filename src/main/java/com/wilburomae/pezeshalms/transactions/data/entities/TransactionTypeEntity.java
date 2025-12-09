package com.wilburomae.pezeshalms.transactions.data.entities;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountEntity;
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

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "transaction_types_debit_accounts", schema = "lms", joinColumns = @JoinColumn(name = "transaction_type_id"), inverseJoinColumns = @JoinColumn(name = "account_id"))
    private Set<AccountEntity> debitAccounts = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "transaction_types_credit_accounts", schema = "lms", joinColumns = @JoinColumn(name = "transaction_type_id"), inverseJoinColumns = @JoinColumn(name = "account_id"))
    private Set<AccountEntity> creditAccounts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "transactionType")
    private Set<TransactionEntity> transactions = new LinkedHashSet<>();

    public void addDebitAccount(AccountEntity entity) {
        Optional<AccountEntity> existing = debitAccounts.stream()
                .filter(t -> t.getDateCreated() != null && t.getId() == entity.getId())
                .findAny();

        if (existing.isPresent()) return;

        debitAccounts.add(entity);
        entity.getDebitTransactionTypes().add(this);
    }

    public void addCreditAccount(AccountEntity entity) {
        Optional<AccountEntity> existing = creditAccounts.stream()
                .filter(t -> t.getDateCreated() != null && t.getId() == entity.getId())
                .findAny();

        if (existing.isPresent()) return;

        creditAccounts.add(entity);
        entity.getCreditTransactionTypes().add(this);
    }
}