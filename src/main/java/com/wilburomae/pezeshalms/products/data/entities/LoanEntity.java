package com.wilburomae.pezeshalms.products.data.entities;

import com.wilburomae.pezeshalms.accounts.data.entities.CurrencyEntity;
import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import com.wilburomae.pezeshalms.users.data.entities.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "loans", schema = "lms")
public class LoanEntity extends IdAuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private UserEntity customer;

    @Column(name = "principal_amount_minor")
    private long principalAmountMinor;

    @Column(name = "access_fee_minor")
    private long accessFeeMinor;

    @Column(name = "total_amount_minor")
    private long totalAmountMinor;

    @Column(name = "current_balance")
    private long currentBalance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency")
    private CurrencyEntity currency;

    @Column(name = "due_date")
    private OffsetDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "status")
    private LoanStatus status;

    @OneToMany(mappedBy = "loan")
    private Set<LoanTransactionEntity> loanTransactions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "loan")
    private Set<LoanRepaymentScheduleEntity> repaymentSchedules = new LinkedHashSet<>();

    public void addLoanTransaction(LoanTransactionEntity loanTransaction) {
        loanTransactions.add(loanTransaction);
        loanTransaction.setLoan(this);
    }

    public void addRepaymentSchedule(LoanRepaymentScheduleEntity repaymentSchedule) {
        repaymentSchedules.add(repaymentSchedule);
        repaymentSchedule.setLoan(this);
    }
}