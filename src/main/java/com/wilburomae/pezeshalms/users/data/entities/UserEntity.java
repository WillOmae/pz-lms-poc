package com.wilburomae.pezeshalms.users.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.util.List;

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
    private List<ContactEntity> contacts;

    @OneToOne(mappedBy = "user")
    private CredentialEntity credential;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<IdentificationEntity> ids;

    @OneToMany(mappedBy = "user")
    private List<LoginAttemptEntity> loginAttempts;

    @OneToMany(mappedBy = "user")
    private List<RefreshTokenEntity> refreshTokens;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private List<RoleEntity> roles;
}