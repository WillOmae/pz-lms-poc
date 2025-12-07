package com.wilburomae.pezeshalms.users.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

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
    private Set<UserEntity> users = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<PermissionEntity> permissions = new LinkedHashSet<>();

    public void addPermission(PermissionEntity permission) {
        permissions.add(permission);
        permission.getRoles().add(this);
    }
}