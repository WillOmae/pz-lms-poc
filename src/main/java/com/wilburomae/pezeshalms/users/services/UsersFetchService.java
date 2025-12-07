package com.wilburomae.pezeshalms.users.services;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.users.data.entities.UserEntity;
import com.wilburomae.pezeshalms.users.data.repositories.UserRepository;
import com.wilburomae.pezeshalms.users.dtos.User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Service
public class UsersFetchService {

    private final UserRepository userRepository;

    public UsersFetchService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Response<List<User>> fetchAll(String contact) {
        List<User> fetched;
        if (contact != null) {
            Optional<UserEntity> found = userRepository.fetchByContact(contact);
            if (found.isEmpty()) {
                return new Response<>(NOT_FOUND, "User not found", Collections.emptyList());
            }
            fetched = found.map(User::from).map(List::of).get();
        } else {
            fetched = userRepository.findAll()
                    .stream()
                    .map(User::from)
                    .toList();
        }
        return new Response<>(OK, "Fetched %d users".formatted(fetched.size()), fetched);
    }

    public Response<User> fetchById(Long id) {
        return userRepository.findById(id)
                .map(entity -> new Response<>(OK, "User found", User.from(entity)))
                .orElseGet(() -> new Response<>(NOT_FOUND, "User not found", null));
    }
}
