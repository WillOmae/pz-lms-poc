package com.wilburomae.pezeshalms.users.services;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.users.data.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.OK;

@Service
public class RolesDeletionService {

    private final RoleRepository roleRepository;

    public RolesDeletionService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Response<Void> delete(Long id) {
        roleRepository.deleteById(id);
        return new Response<>(OK, "Deleted successfully", null);
    }
}
