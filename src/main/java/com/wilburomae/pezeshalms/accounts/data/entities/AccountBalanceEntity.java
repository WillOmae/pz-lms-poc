package com.wilburomae.pezeshalms.accounts.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "account_balances", schema = "lms")
public class AccountBalanceEntity extends IdAuditableEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id")
    private CurrencyEntity currency;

    @Column(name = "balance_minor")
    private long balanceMinor;

    @Column(name = "upper_threshold_minor")
    private Long upperThresholdMinor;

    @Column(name = "lower_threshold_minor")
    private Long lowerThresholdMinor;
}