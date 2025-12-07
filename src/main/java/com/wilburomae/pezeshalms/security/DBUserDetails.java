package com.wilburomae.pezeshalms.security;

import com.wilburomae.pezeshalms.users.dtos.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DBUserDetails implements UserDetails {

    private final User user;

    public DBUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (var role : user.roles()) {
            for (var permission : role.permissions()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + permission));
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.credential().hashedPassword();
    }

    @Override
    public String getUsername() {
        return user.name();
    }

    @Override
    public boolean isEnabled() {
        return "ACTIVE".equalsIgnoreCase(user.credential().status());
    }
}
