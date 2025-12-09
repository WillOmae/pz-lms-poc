package com.wilburomae.pezeshalms.products.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import com.wilburomae.pezeshalms.transactions.data.entities.ReasonTypeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_reason_types", schema = "lms")
public class ProductReasonTypeEntity extends IdAuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_product_id")
    private LoanProductEntity loanProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reason_type_id")
    private ReasonTypeEntity reasonType;
}