package com.wilburomae.pezeshalms.integrationtests;

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

    private final String baseUrl = "/users";
    private final List<RoleEntity> roles = new ArrayList<>();
    private final Supplier<String> nameSupplier = () -> "USER_" + System.nanoTime() + RANDOM.nextInt(100, 1000);

    @Autowired
    RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
        if (roles.isEmpty()) {
            roles.addAll(roleRepository.findAll());
        }
    }

    @Test
    void whenCreateNewUser_thenReturnHttp201() throws Exception {
        createUserRequest(nameSupplier.get());
    }

    @Test
    void whenCreateDuplicate_thenReturnHttp409() throws Exception {
        Map.Entry<Long, UserRequest> created = createUserRequest(nameSupplier.get());

        Long result = integrationTestHelper.create(baseUrl, created.getValue(), Long.class, CONFLICT);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetchExistingById_thenReturnHttp200() throws Exception {
        Map.Entry<Long, UserRequest> created = createUserRequest(nameSupplier.get());

        Role result = integrationTestHelper.fetchById(baseUrl, created.getKey(), emptyMap(), Role.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenFetchNonExistentById_thenReturnHttp404() throws Exception {
        Map.Entry<Long, UserRequest> created = createUserRequest(nameSupplier.get());

        Role result = integrationTestHelper.fetchById(baseUrl, created.getKey() + 1, emptyMap(), Role.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetch_thenReturnHttp200() throws Exception {
        Collection<Role> result = integrationTestHelper.fetch(baseUrl, emptyMap(), Role.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, UserRequest> created = createUserRequest(nameSupplier.get());

        Long result = integrationTestHelper.update(baseUrl, created.getKey() + 1, created.getValue(), Long.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, UserRequest> created = createUserRequest(nameSupplier.get());

        Void result = integrationTestHelper.delete(baseUrl, created.getKey(), Void.class, OK);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, UserRequest> created = createUserRequest(nameSupplier.get());

        Void result = integrationTestHelper.delete(baseUrl, created.getKey() + 1, Void.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    private Map.Entry<Long, UserRequest> createUserRequest(String name) throws Exception {
        Contact contact = new Contact(name + "@test.com", "EMAIL", true);
        Identification identification = new Identification(String.valueOf(RANDOM.nextInt(100000, 1000000)), 1L, "National ID");
        List<Long> ids = roles.stream().map(RoleEntity::getId).toList();
        UserRequest userRequest = new UserRequest(name, "CUSTOMER", List.of(contact), List.of(identification), ids);
        Long result = integrationTestHelper.create(baseUrl, userRequest, Long.class, CREATED);
        Assertions.assertNotNull(result);
        return Map.entry(result, userRequest);
    }
}
