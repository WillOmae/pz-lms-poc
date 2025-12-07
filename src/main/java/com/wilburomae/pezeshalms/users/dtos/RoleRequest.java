package com.wilburomae.pezeshalms.users.dtos;

import java.util.List;

public record RoleRequest(String name, List<Long> permissionIds) {
}
