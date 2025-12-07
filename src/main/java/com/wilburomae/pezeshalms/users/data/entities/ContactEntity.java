package com.wilburomae.pezeshalms.users.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "entity_contacts", schema = "lms")
public class ContactEntity extends IdAuditableEntity {

    @Column(name = "contact")
    private String contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id")
    private UserEntity user;

    @Column(name = "is_primary")
    private boolean isPrimary;

    @Column(name = "contact_type")
    private String contactType;
}