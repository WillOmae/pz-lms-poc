package com.wilburomae.pezeshalms.security;

import com.wilburomae.pezeshalms.users.data.entities.PermissionEntity;
import com.wilburomae.pezeshalms.users.data.entities.RoleEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DBUserDetails implements UserDetails {

    private final CredentialEntity credential;

    public DBUserDetails(CredentialEntity credential) {
        this.credential = credential;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (RoleEntity role : credential.getUser().getRoles()) {
            for (PermissionEntity permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + permission.getName()));
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return credential.getHashedPassword();
    }

    @Override
    public String getUsername() {
        return credential.getUser().getName();
    }

    @Override
    public boolean isEnabled() {
        return "ACTIVE".equalsIgnoreCase(credential.getStatus().getName());
    }
}
