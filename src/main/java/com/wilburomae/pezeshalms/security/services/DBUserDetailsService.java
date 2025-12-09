package com.wilburomae.pezeshalms.security.services;

import com.wilburomae.pezeshalms.security.dtos.DBUserDetails;
import com.wilburomae.pezeshalms.users.data.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DBUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    public DBUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.fetchByContact(username)
                .map(DBUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
