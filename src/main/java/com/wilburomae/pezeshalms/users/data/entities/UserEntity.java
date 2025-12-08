package com.wilburomae.pezeshalms.users.data.entities;

import com.wilburomae.pezeshalms.accounts.data.entities.PartnerAccountEntity;
import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import com.wilburomae.pezeshalms.security.CredentialEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "entities", schema = "lms")
public class UserEntity extends IdAuditableEntity {

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "entity_type")
    private EntityType type;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<ContactEntity> contacts = new LinkedHashSet<>();

    @OneToOne(mappedBy = "user")
    private CredentialEntity credential;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<IdentificationEntity> ids = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<LoginAttemptEntity> loginAttempts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<RefreshTokenEntity> refreshTokens = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<RoleEntity> roles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<PartnerAccountEntity> partnerAccounts = new LinkedHashSet<>();
}