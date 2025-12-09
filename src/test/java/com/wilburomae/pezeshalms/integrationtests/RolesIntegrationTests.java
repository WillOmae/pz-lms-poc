package com.wilburomae.pezeshalms.integrationtests;

import com.wilburomae.pezeshalms.users.data.entities.PermissionEntity;
import com.wilburomae.pezeshalms.users.data.repositories.PermissionRepository;
import com.wilburomae.pezeshalms.users.dtos.Role;
import com.wilburomae.pezeshalms.users.dtos.RoleRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Supplier;

import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.*;

public class RolesIntegrationTests extends BaseIntegrationTests {

    private static final Random RANDOM = new SecureRandom();

    private final String baseUrl = "/roles";
    private final List<PermissionEntity> permissions = new ArrayList<>();
    private final Supplier<String> nameSupplier = () -> "ROLE_" + System.nanoTime() + RANDOM.nextInt(100, 1000);

    @Autowired
    PermissionRepository permissionRepository;

    @BeforeEach
    public void setUp() {
        if (permissions.isEmpty()) {
            permissions.addAll(permissionRepository.findAll());
        }
    }

    @Test
    void whenCreateNewRole_thenReturnHttp201() throws Exception {
        createRoleRequest(nameSupplier.get());
    }

    @Test
    void whenCreateDuplicate_thenReturnHttp409() throws Exception {
        Map.Entry<Long, RoleRequest> created = createRoleRequest(nameSupplier.get());

        Long result = integrationTestHelper.create(baseUrl, created.getValue(), Long.class, CONFLICT);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetchExistingById_thenReturnHttp200() throws Exception {
        Map.Entry<Long, RoleRequest> created = createRoleRequest(nameSupplier.get());

        Role result = integrationTestHelper.fetchById(baseUrl, created.getKey(), emptyMap(), Role.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenFetchNonExistentById_thenReturnHttp404() throws Exception {
        Map.Entry<Long, RoleRequest> created = createRoleRequest(nameSupplier.get());

        Role result = integrationTestHelper.fetchById(baseUrl, created.getKey() + 1, emptyMap(), Role.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetch_thenReturnHttp200() throws Exception {
        Collection<Role> result = integrationTestHelper.fetch(baseUrl, emptyMap(), Role.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, RoleRequest> created = createRoleRequest(nameSupplier.get());

        Long result = integrationTestHelper.update(baseUrl, created.getKey(), created.getValue(), Long.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, RoleRequest> created = createRoleRequest(nameSupplier.get());

        Long result = integrationTestHelper.update(baseUrl, created.getKey() + 1, created.getValue(), Long.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, RoleRequest> created = createRoleRequest(nameSupplier.get());

        Void result = integrationTestHelper.delete(baseUrl, created.getKey(), Void.class, OK);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, RoleRequest> created = createRoleRequest(nameSupplier.get());

        Void result = integrationTestHelper.delete(baseUrl, created.getKey() + 1, Void.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    private Map.Entry<Long, RoleRequest> createRoleRequest(String name) throws Exception {
        List<Long> ids = permissions.stream().map(PermissionEntity::getId).toList();
        RoleRequest roleRequest = new RoleRequest(name, "Description for " + name, ids);
        Long result = integrationTestHelper.create(baseUrl, roleRequest, Long.class, CREATED);
        Assertions.assertNotNull(result);
        integrationTestHelper.fetchById(baseUrl, result, emptyMap(), Role.class, OK);
        return Map.entry(result, roleRequest);
    }
}
