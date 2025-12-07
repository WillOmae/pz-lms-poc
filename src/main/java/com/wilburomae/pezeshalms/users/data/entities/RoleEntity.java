package com.wilburomae.pezeshalms.users.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "roles", schema = "lms")
public class RoleEntity extends IdAuditableEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany
    @JoinTable(name = "user_roles", schema = "lms", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "entity_id"))
    private List<UserEntity> users;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private List<PermissionEntity> permissions;
}