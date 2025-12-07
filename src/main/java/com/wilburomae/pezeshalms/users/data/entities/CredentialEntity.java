package com.wilburomae.pezeshalms.users.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "entity_credentials", schema = "lms")
public class CredentialEntity extends IdAuditableEntity {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entity_id")
    private UserEntity user;

    @Column(name = "hashed_password")
    private String hashedPassword;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "credentials_status_id")
    private CredentialStatusEntity status;
}