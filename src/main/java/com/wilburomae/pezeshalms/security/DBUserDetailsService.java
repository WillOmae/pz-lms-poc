package com.wilburomae.pezeshalms.security;

import com.wilburomae.pezeshalms.users.data.repositories.UserRepository;
import com.wilburomae.pezeshalms.users.dtos.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DBUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DBUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.fetchByContact(username)
                .map(User::from)
                .map(DBUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
