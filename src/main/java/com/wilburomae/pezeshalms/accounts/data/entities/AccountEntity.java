package com.wilburomae.pezeshalms.accounts.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "accounts", schema = "lms")
public class AccountEntity extends IdAuditableEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_type_id")
    private AccountTypeEntity accountType;

    @Column(name = "overdrawable")
    private boolean overdrawable;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_status_id")
    private AccountStatusEntity accountStatus;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<AccountBalanceEntity> accountBalances = new LinkedHashSet<>();

    @OneToMany(mappedBy = "account")
    private Set<PartnerAccountEntity> partnerAccounts = new LinkedHashSet<>();

    public void addBalance(AccountBalanceEntity balance) {
        Optional<AccountBalanceEntity> existing = accountBalances.stream()
                .filter(e -> e.getDateCreated() != null && e.getId() == balance.getId())
                .findAny();

        if (existing.isPresent()) {
            AccountBalanceEntity existingBalance = existing.get();
            existingBalance.setAccount(this);
            existingBalance.setCurrency(balance.getCurrency());
            existingBalance.setBalanceMinor(balance.getBalanceMinor());
            existingBalance.setUpperThresholdMinor(balance.getUpperThresholdMinor());
            existingBalance.setLowerThresholdMinor(balance.getLowerThresholdMinor());
        } else {
            accountBalances.add(balance);
            balance.setAccount(this);
        }
    }
}