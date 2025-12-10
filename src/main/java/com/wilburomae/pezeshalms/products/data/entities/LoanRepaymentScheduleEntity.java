package com.wilburomae.pezeshalms.products.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "loan_repayment_schedules", schema = "lms")
public class LoanRepaymentScheduleEntity extends IdAuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    private LoanEntity loan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private TransactionEntity transaction;

    @Column(name = "principal_portion_minor")
    private long principalPortionMinor;

    @Column(name = "interest_portion_minor")
    private long interestPortionMinor;

    @Column(name = "current_balance")
    private long currentBalance;
}