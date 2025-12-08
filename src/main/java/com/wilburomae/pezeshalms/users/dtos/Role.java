package com.wilburomae.pezeshalms.users.dtos;

import com.wilburomae.pezeshalms.users.data.entities.PermissionEntity;
import com.wilburomae.pezeshalms.users.data.entities.RoleEntity;

import java.util.List;

public record Role(long id, String name, List<String> permissions) {

    public static Role from(RoleEntity entity) {
        List<String> permissions = entity.getPermissions().stream().map(PermissionEntity::getName).toList();
        return new Role(entity.getId(), entity.getName(), permissions);
    }
}
