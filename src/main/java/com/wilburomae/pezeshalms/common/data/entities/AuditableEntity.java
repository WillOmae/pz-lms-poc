package com.wilburomae.pezeshalms.common.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditableEntity {

    @Column(name = "date_created")
    private OffsetDateTime dateCreated;

    @Column(name = "date_updated")
    private OffsetDateTime dateUpdated;

    @PrePersist
    protected void onCreate() {
        this.dateCreated = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dateUpdated = OffsetDateTime.now();
    }
}
