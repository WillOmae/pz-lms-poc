package com.wilburomae.pezeshalms.users.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "entity_ids", schema = "lms")
public class IdentificationEntity extends IdAuditableEntity {

    @Column(name = "id_number")
    private String idNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_type")
    private IdentificationTypeEntity idType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id")
    private UserEntity user;
}