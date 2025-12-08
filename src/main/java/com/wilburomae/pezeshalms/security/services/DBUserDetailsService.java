package com.wilburomae.pezeshalms.security.services;

import com.wilburomae.pezeshalms.security.data.repositories.CredentialRepository;
import com.wilburomae.pezeshalms.security.dtos.DBUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DBUserDetailsService implements UserDetailsService {

    private final CredentialRepository repository;

    public DBUserDetailsService(CredentialRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.fetchByContact(username)
                .map(DBUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
