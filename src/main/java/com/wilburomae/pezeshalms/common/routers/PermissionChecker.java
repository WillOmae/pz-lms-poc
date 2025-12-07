package com.wilburomae.pezeshalms.common.routers;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public final class PermissionChecker {

    private PermissionChecker() {
    }

    public static void validatePermission(String permission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        permission = "ROLE_" + permission;
        boolean hasPermission = auth != null && auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(permission::equals);

        if (!hasPermission) {
            throw new AccessDeniedException("Not authorized");
        }
    }
}
