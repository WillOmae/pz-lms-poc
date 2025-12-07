package com.wilburomae.pezeshalms.accounts.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "account_types", schema = "lms")
public class AccountTypeEntity extends IdAuditableEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "accountType")
    private Set<AccountEntity> accounts = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_account_type_id")
    private AccountTypeEntity parentAccountType;

    @OneToMany
    @JoinColumn(name = "parent_account_type_id")
    private Set<AccountTypeEntity> accountTypes = new LinkedHashSet<>();
}