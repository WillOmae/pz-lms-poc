package com.wilburomae.pezeshalms.accounts.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionEntryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "currencies", schema = "lms")
public class CurrencyEntity extends IdAuditableEntity {

    @Column(name = "code")
    private String code;

    @Column(name = "numeric_code")
    private String numericCode;

    @Column(name = "name")
    private String name;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "decimal_places")
    private short decimalPlaces;

    @Column(name = "active")
    private boolean active;

    @OneToMany(mappedBy = "currency")
    private Set<AccountBalanceEntity> accountBalances = new LinkedHashSet<>();

    @OneToMany(mappedBy = "currency")
    private Set<TransactionEntryEntity> transactionEntries = new LinkedHashSet<>();
}