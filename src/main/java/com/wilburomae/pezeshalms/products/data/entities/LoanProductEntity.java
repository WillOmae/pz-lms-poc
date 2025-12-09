package com.wilburomae.pezeshalms.products.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import com.wilburomae.pezeshalms.transactions.data.entities.ReasonTypeEntity;
import com.wilburomae.pezeshalms.users.data.entities.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Optional;
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

    @Column(name = "interest_chargeable")
    private boolean interestChargeable;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "interest_term_unit")
    private TermUnit interestTermUnit;

    @Column(name = "access_fee_chargeable")
    private boolean accessFeeChargeable;

    @Column(name = "access_fee_percentage")
    private BigDecimal accessFeePercentage;

    @Column(name = "term")
    private int term;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "term_unit")
    private TermUnit termUnit;

    @Column(name = "max_disbursements_per_customer")
    private int maxDisbursementsPerCustomer;

    @Column(name = "active")
    private boolean active;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "loan_product_partners", schema = "lms", joinColumns = @JoinColumn(name = "loan_product_id"), inverseJoinColumns = @JoinColumn(name = "entity_id"))
    private Set<UserEntity> partners = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "product_reason_types", schema = "lms", joinColumns = @JoinColumn(name = "loan_product_id"), inverseJoinColumns = @JoinColumn(name = "reason_type_id"))
    private Set<ReasonTypeEntity> reasonTypes = new LinkedHashSet<>();

    public void addReasonType(ReasonTypeEntity entity) {
        Optional<ReasonTypeEntity> existing = reasonTypes.stream()
                .filter(t -> t.getDateCreated() != null && t.getId() == entity.getId())
                .findAny();

        if (existing.isPresent()) return;

        reasonTypes.add(entity);
        entity.getLoanProducts().add(this);
    }

    public void addPartner(UserEntity entity) {
        Optional<UserEntity> existing = partners.stream()
                .filter(t -> t.getDateCreated() != null && t.getId() == entity.getId())
                .findAny();

        if (existing.isPresent()) return;

        partners.add(entity);
        entity.getLoanProducts().add(this);
    }
}