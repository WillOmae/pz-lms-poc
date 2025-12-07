package com.wilburomae.pezeshalms.users.services;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import com.wilburomae.pezeshalms.users.data.entities.PermissionEntity;
import com.wilburomae.pezeshalms.users.data.entities.RoleEntity;
import com.wilburomae.pezeshalms.users.data.repositories.PermissionRepository;
import com.wilburomae.pezeshalms.users.data.repositories.RoleRepository;
import com.wilburomae.pezeshalms.users.dtos.RoleRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class RolesUpsertService implements UpsertService<RoleRequest> {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RolesUpsertService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Transactional
    @Override
    public Response<Long> upsert(Long id, RoleRequest request) {
        RoleEntity entity = new RoleEntity();
        if (id != null) {
            Optional<RoleEntity> found = roleRepository.findById(id);
            if (found.isEmpty()) {
                return new Response<>(NOT_FOUND, "Role not found", null);
            } else {
                entity = found.get();
            }
        }

        List<PermissionEntity> foundPermissions = permissionRepository.findAllById(request.permissionIds());
        if (foundPermissions.size() != request.permissionIds().size()) {
            return new Response<>(NOT_FOUND, "Permissions not found", null);
        }

        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.getPermissions().clear();
        foundPermissions.forEach(entity::addPermission);

        entity = roleRepository.save(entity);

        return new Response<>(CREATED, "Role saved successfully", entity.getId());
    }
}
