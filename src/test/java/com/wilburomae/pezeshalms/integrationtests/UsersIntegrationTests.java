package com.wilburomae.pezeshalms.integrationtests;

import com.wilburomae.pezeshalms.helpers.IntegrationTestHelper;
import com.wilburomae.pezeshalms.users.data.entities.RoleEntity;
import com.wilburomae.pezeshalms.users.data.repositories.RoleRepository;
import com.wilburomae.pezeshalms.users.dtos.Contact;
import com.wilburomae.pezeshalms.users.dtos.Identification;
import com.wilburomae.pezeshalms.users.dtos.Role;
import com.wilburomae.pezeshalms.users.dtos.UserRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Supplier;

import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.*;

public class UsersIntegrationTests extends BaseIntegrationTests {

    private static final Random RANDOM = new SecureRandom();
    private static final String BASE_URL = "/users";

    private RoleGenerator roleGenerator;

    @Autowired
    RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
        if (roleGenerator == null) {
            roleGenerator = new RoleGenerator(integrationTestHelper, roleRepository);
        }
    }

    @Test
    void whenCreateNew_thenReturnHttp201() throws Exception {
        roleGenerator.createRequest();
    }

    @Test
    void whenCreateDuplicate_thenReturnHttp409() throws Exception {
        Map.Entry<Long, UserRequest> created = roleGenerator.createRequest();

        Long result = integrationTestHelper.create(BASE_URL, created.getValue(), Long.class, CONFLICT);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetchExistingById_thenReturnHttp200() throws Exception {
        Map.Entry<Long, UserRequest> created = roleGenerator.createRequest();

        Role result = integrationTestHelper.fetchById(BASE_URL, created.getKey(), emptyMap(), Role.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenFetchNonExistentById_thenReturnHttp404() throws Exception {
        Map.Entry<Long, UserRequest> created = roleGenerator.createRequest();

        Role result = integrationTestHelper.fetchById(BASE_URL, created.getKey() + 1, emptyMap(), Role.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetch_thenReturnHttp200() throws Exception {
        Collection<Role> result = integrationTestHelper.fetch(BASE_URL, emptyMap(), Role.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, UserRequest> created = roleGenerator.createRequest();

        Long result = integrationTestHelper.update(BASE_URL, created.getKey() + 1, created.getValue(), Long.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, UserRequest> created = roleGenerator.createRequest();

        Void result = integrationTestHelper.delete(BASE_URL, created.getKey(), Void.class, OK);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, UserRequest> created = roleGenerator.createRequest();

        Void result = integrationTestHelper.delete(BASE_URL, created.getKey() + 1, Void.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    public static class RoleGenerator {

        private final IntegrationTestHelper integrationTestHelper;
        private final List<RoleEntity> roles = new ArrayList<>();
        private final Supplier<String> nameSupplier = () -> "USER_" + System.nanoTime() + RANDOM.nextInt(100, 1000);

        public RoleGenerator(IntegrationTestHelper integrationTestHelper, RoleRepository roleRepository) {
            this.integrationTestHelper = integrationTestHelper;
            this.roles.addAll(roleRepository.findAll());
        }

        public Map.Entry<Long, UserRequest> createRequest() throws Exception {
            String name = nameSupplier.get();
            Contact contact = new Contact(name + "@test.com", "EMAIL", true);
            Identification identification = new Identification(String.valueOf(RANDOM.nextInt(100000, 1000000)), 1L, "National ID");
            List<Long> ids = roles.stream().map(RoleEntity::getId).toList();
            UserRequest request = new UserRequest(name, "CUSTOMER", List.of(contact), List.of(identification), ids);
            Long result = integrationTestHelper.create(BASE_URL, request, Long.class, CREATED);
            Assertions.assertNotNull(result);
            return Map.entry(result, request);
        }
    }
}
