package com.wilburomae.pezeshalms.users.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
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
@Table(name = "id_types", schema = "lms")
public class IdentificationTypeEntity extends IdAuditableEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "idType")
    private Set<IdentificationEntity> ids = new LinkedHashSet<>();
}