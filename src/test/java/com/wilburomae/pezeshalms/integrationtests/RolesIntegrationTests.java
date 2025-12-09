package com.wilburomae.pezeshalms.integrationtests;

import com.wilburomae.pezeshalms.helpers.IntegrationTestHelper;
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
    private static final String BASE_URL = "/roles";

    private RoleGenerator roleGenerator;

    @Autowired
    PermissionRepository permissionRepository;

    @BeforeEach
    public void setUp() {
        if (roleGenerator == null) {
            roleGenerator = new RoleGenerator(integrationTestHelper, permissionRepository);
        }
    }

    @Test
    void whenCreateNew_thenReturnHttp201() throws Exception {
        roleGenerator.createRequest();
    }

    @Test
    void whenCreateDuplicate_thenReturnHttp409() throws Exception {
        Map.Entry<Long, RoleRequest> created = roleGenerator.createRequest();

        Long result = integrationTestHelper.create(BASE_URL, created.getValue(), Long.class, CONFLICT);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetchExistingById_thenReturnHttp200() throws Exception {
        Map.Entry<Long, RoleRequest> created = roleGenerator.createRequest();

        Role result = integrationTestHelper.fetchById(BASE_URL, created.getKey(), emptyMap(), Role.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenFetchNonExistentById_thenReturnHttp404() throws Exception {
        Map.Entry<Long, RoleRequest> created = roleGenerator.createRequest();

        Role result = integrationTestHelper.fetchById(BASE_URL, created.getKey() + 1, emptyMap(), Role.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetch_thenReturnHttp200() throws Exception {
        Collection<Role> result = integrationTestHelper.fetch(BASE_URL, emptyMap(), Role.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, RoleRequest> created = roleGenerator.createRequest();

        Long result = integrationTestHelper.update(BASE_URL, created.getKey(), created.getValue(), Long.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, RoleRequest> created = roleGenerator.createRequest();

        Long result = integrationTestHelper.update(BASE_URL, created.getKey() + 1, created.getValue(), Long.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, RoleRequest> created = roleGenerator.createRequest();

        Void result = integrationTestHelper.delete(BASE_URL, created.getKey(), Void.class, OK);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, RoleRequest> created = roleGenerator.createRequest();

        Void result = integrationTestHelper.delete(BASE_URL, created.getKey() + 1, Void.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    public static class RoleGenerator {

        private final IntegrationTestHelper integrationTestHelper;
        private final List<PermissionEntity> permissions = new ArrayList<>();
        private final Supplier<String> nameSupplier = () -> "ROLE_" + System.nanoTime() + RANDOM.nextInt(100, 1000);

        public RoleGenerator(IntegrationTestHelper integrationTestHelper, PermissionRepository permissionRepository) {
            this.integrationTestHelper = integrationTestHelper;
            this.permissions.addAll(permissionRepository.findAll());
        }

        public Map.Entry<Long, RoleRequest> createRequest() throws Exception {
            String name = nameSupplier.get();
            List<Long> ids = permissions.stream().map(PermissionEntity::getId).toList();
            RoleRequest request = new RoleRequest(name, "Description for " + name, ids);
            Long result = integrationTestHelper.create(BASE_URL, request, Long.class, CREATED);
            Assertions.assertNotNull(result);
            return Map.entry(result, request);
        }
    }
}
