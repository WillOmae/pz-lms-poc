package com.wilburomae.pezeshalms.users.services;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import com.wilburomae.pezeshalms.users.data.entities.EntityType;
import com.wilburomae.pezeshalms.users.data.entities.IdentificationTypeEntity;
import com.wilburomae.pezeshalms.users.data.entities.RoleEntity;
import com.wilburomae.pezeshalms.users.data.entities.UserEntity;
import com.wilburomae.pezeshalms.users.data.repositories.IdentificationTypeRepository;
import com.wilburomae.pezeshalms.users.data.repositories.RoleRepository;
import com.wilburomae.pezeshalms.users.data.repositories.UserRepository;
import com.wilburomae.pezeshalms.users.dtos.Identification;
import com.wilburomae.pezeshalms.users.dtos.UserRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class UsersUpsertService implements UpsertService<UserRequest> {

    private final UserRepository userRepository;
    private final IdentificationTypeRepository identificationTypeRepository;
    private final RoleRepository roleRepository;

    public UsersUpsertService(UserRepository userRepository, IdentificationTypeRepository identificationTypeRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.identificationTypeRepository = identificationTypeRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public Response<Long> upsert(Long id, UserRequest request) {
        Response<UserEntity> initResponse = initEntity(id, userRepository, UserEntity::new);
        if (initResponse.responseCode().isError()) {
            return new Response<>(NOT_FOUND, "Role not found", null);
        }
        UserEntity entity = initResponse.data();

        entity.setName(request.name());
        entity.setType(EntityType.valueOf(request.type()));

        entity.getContacts().clear();
        request.contacts().forEach(entity::addContact);

        List<Long> idTypeIds = request.identifications().stream().map(Identification::idTypeId).toList();
        List<IdentificationTypeEntity> foundIdTypes = identificationTypeRepository.findAllById(idTypeIds);
        if (foundIdTypes.size() != idTypeIds.size()) {
            return new Response<>(NOT_FOUND, "Identification types not found", null);
        }
        entity.getIds().clear();
        for (Identification e : request.identifications()) {
            entity.addIdentification(e, foundIdTypes);
        }

        List<RoleEntity> foundRoles = roleRepository.findAllById(request.roleIds());
        if (foundRoles.size() != request.roleIds().size()) {
            return new Response<>(NOT_FOUND, "Roles not found", null);
        }
        entity.getRoles().clear();
        foundRoles.forEach(entity::addRole);

        entity = userRepository.save(entity);

        return successResponse(id, "User", entity);
    }
}
