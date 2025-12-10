package com.wilburomae.pezeshalms.transactions.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import com.wilburomae.pezeshalms.products.data.entities.LoanRepaymentScheduleEntity;
import com.wilburomae.pezeshalms.products.data.entities.LoanTransactionEntity;
import com.wilburomae.pezeshalms.users.data.entities.UserEntity;
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

    @Column(name = "idempotency_key")
    private String idempotencyKey;

    @Column(name = "transaction_reference")
    private String transactionReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opposite_party_id")
    private UserEntity oppositeParty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_type_id")
    private TransactionTypeEntity transactionType;

    @OneToOne
    @JoinColumn(name = "linked_transaction_id")
    private TransactionEntity originalTransaction;

    @OneToMany(mappedBy = "transaction")
    private Set<TransactionEntryEntity> transactionEntries = new LinkedHashSet<>();

    @OneToMany(mappedBy = "transaction")
    private Set<LoanTransactionEntity> loanTransactions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "transaction")
    private Set<LoanRepaymentScheduleEntity> repaymentSchedules = new LinkedHashSet<>();
}