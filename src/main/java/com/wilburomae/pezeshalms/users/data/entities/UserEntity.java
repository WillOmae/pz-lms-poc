package com.wilburomae.pezeshalms.users.data.entities;

import com.wilburomae.pezeshalms.accounts.data.entities.PartnerAccountEntity;
import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import com.wilburomae.pezeshalms.products.data.entities.LoanProductEntity;
import com.wilburomae.pezeshalms.security.data.entities.CredentialEntity;
import com.wilburomae.pezeshalms.users.dtos.Contact;
import com.wilburomae.pezeshalms.users.dtos.Identification;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.util.LinkedHashSet;
import java.util.List;
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

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ContactEntity> contacts = new LinkedHashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private CredentialEntity credential;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<IdentificationEntity> ids = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<LoginAttemptEntity> loginAttempts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<RefreshTokenEntity> refreshTokens = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<RoleEntity> roles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<PartnerAccountEntity> partnerAccounts = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "partners")
    private Set<LoanProductEntity> loanProducts = new LinkedHashSet<>();

    public void addRole(RoleEntity role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    public void addContact(Contact contact) {
        ContactEntity entity = new ContactEntity();
        entity.setContact(contact.contact());
        entity.setUser(this);
        entity.setPrimary(contact.isPrimary());
        entity.setContactType(contact.contactType());

        contacts.add(entity);
    }

    public void addIdentification(Identification identification, List<IdentificationTypeEntity> idTypes) {
        IdentificationTypeEntity idType = idTypes.stream()
                .filter(e -> e.getId() == identification.idTypeId())
                .findFirst()
                .orElseThrow();

        IdentificationEntity entity = new IdentificationEntity();
        entity.setIdNumber(identification.idNumber());
        entity.setIdType(idType);
        entity.setUser(this);

        ids.add(entity);
    }

    public void addCredential(CredentialEntity credential) {
        this.credential = credential;
        credential.setUser(this);
    }
}