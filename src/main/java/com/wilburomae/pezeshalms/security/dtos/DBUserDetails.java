package com.wilburomae.pezeshalms.security.dtos;

import com.wilburomae.pezeshalms.users.data.entities.PermissionEntity;
import com.wilburomae.pezeshalms.users.data.entities.RoleEntity;
import com.wilburomae.pezeshalms.users.data.entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DBUserDetails implements UserDetails {

    private final UserEntity user;

    public DBUserDetails(UserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (RoleEntity role : user.getRoles()) {
            for (PermissionEntity permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + permission.getName()));
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getCredential().getHashedPassword();
    }

    @Override
    public String getUsername() {
        return user.getCredential().getUser().getName();
    }

    @Override
    public boolean isEnabled() {
        return "ACTIVE".equalsIgnoreCase(user.getCredential().getStatus().getName());
    }
}
