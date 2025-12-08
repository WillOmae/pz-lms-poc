package com.wilburomae.pezeshalms.products.data.entities;

import com.wilburomae.pezeshalms.accounts.data.entities.CurrencyEntity;
import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "loan_products", schema = "lms")
public class LoanProductEntity extends IdAuditableEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_code")
    private CurrencyEntity currency;

    @NotNull
    @Column(name = "min_loan_amount_minor", nullable = false)
    private Long minLoanAmountMinor;

    @NotNull
    @Column(name = "max_loan_amount_minor", nullable = false)
    private Long maxLoanAmountMinor;

    @NotNull
    @Column(name = "interest_chargeable", nullable = false)
    private boolean interestChargeable;

    @Column(name = "interest_rate", precision = 5, scale = 4)
    private BigDecimal interestRate;

    @NotNull
    @Column(name = "access_fee_chargeable", nullable = false)
    private boolean accessFeeChargeable;

    @Column(name = "access_fee_amount_minor")
    private Long accessFeeAmountMinor;

    @Column(name = "access_fee_percentage", precision = 5, scale = 4)
    private BigDecimal accessFeePercentage;

    @NotNull
    @Column(name = "term", nullable = false)
    private int term;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "interest_term_unit")
    private TermUnit interestTermUnit;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "term_unit")
    private TermUnit termUnit;

    @OneToMany(mappedBy = "loanProduct")
    private Set<LoanProductPartnerEntity> loanProductPartner = new LinkedHashSet<>();

    @OneToMany(mappedBy = "loanProduct")
    private Set<ProductTransactionTypeEntity> productTransactionType = new LinkedHashSet<>();
}