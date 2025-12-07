package com.wilburomae.pezeshalms.security;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.users.dtos.User;
import com.wilburomae.pezeshalms.users.services.UsersFetchService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class DBUserDetailsService implements UserDetailsService {

    private final UsersFetchService usersFetchService;

    public DBUserDetailsService(UsersFetchService usersFetchService) {
        this.usersFetchService = usersFetchService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Response<List<User>> response = usersFetchService.fetchAll(username);
        if (response.responseCode().is2xxSuccessful()) {
            return new DBUserDetails(response.data().getFirst());
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
