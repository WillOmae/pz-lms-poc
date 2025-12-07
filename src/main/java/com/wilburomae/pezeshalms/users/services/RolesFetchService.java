package com.wilburomae.pezeshalms.users.services;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.users.data.repositories.RoleRepository;
import com.wilburomae.pezeshalms.users.dtos.Role;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Service
public class RolesFetchService {

    private final RoleRepository roleRepository;

    public RolesFetchService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Response<List<Role>> fetchAll() {
        List<Role> fetched = roleRepository.findAll()
                .stream()
                .map(Role::from)
                .toList();

        return new Response<>(OK, "Fetched %d roles".formatted(fetched.size()), fetched);
    }

    public Response<Role> fetchById(long id) {
        return roleRepository.findById(id)
                .map(entity -> new Response<>(OK, "Role found", Role.from(entity)))
                .orElseGet(() -> new Response<>(NOT_FOUND, "Role not found", null));
    }
}
