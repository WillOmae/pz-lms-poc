package com.wilburomae.pezeshalms.transactions.data.entities;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountEntity;
import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import com.wilburomae.pezeshalms.users.data.entities.PermissionEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "transaction_type_components", schema = "lms")
public class TransactionTypeComponentEntity extends IdAuditableEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_type_id")
    private TransactionTypeEntity transactionType;

    @Column(name = "execution_order")
    private short executionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    private PermissionEntity permission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debit_account_id")
    private AccountEntity debitAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_account_id")
    private AccountEntity creditAccount;

    @OneToMany(mappedBy = "transactionTypeComponent")
    private Set<TransactionEntryEntity> transactionEntries = new LinkedHashSet<>();
}