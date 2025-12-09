package com.wilburomae.pezeshalms.integrationtests;

import com.wilburomae.pezeshalms.accounts.dtos.AccountType;
import com.wilburomae.pezeshalms.accounts.dtos.AccountTypeRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.*;

public class AccountTypesIntegrationTests extends BaseIntegrationTests {

    private static final Random RANDOM = new SecureRandom();

    private final String baseUrl = "/accounts/types";
    private final Supplier<String> nameSupplier = () -> "ACCOUNT_TYPES_" + System.nanoTime() + RANDOM.nextInt(100, 1000);

    @Test
    void whenCreateNew_thenReturnHttp201() throws Exception {
        Map.Entry<Long, AccountTypeRequest> parent = createAccountTypeRequest(nameSupplier.get(), null);
        createAccountTypeRequest(nameSupplier.get(), parent.getKey());
    }

    @Test
    void whenCreateDuplicate_thenReturnHttp409() throws Exception {
        Map.Entry<Long, AccountTypeRequest> parent = createAccountTypeRequest(nameSupplier.get(), null);
        Map.Entry<Long, AccountTypeRequest> child = createAccountTypeRequest(nameSupplier.get(), parent.getKey());

        Long result = integrationTestHelper.create(baseUrl, child.getValue(), Long.class, CONFLICT);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetchExistingById_thenReturnHttp200() throws Exception {
        Map.Entry<Long, AccountTypeRequest> parent = createAccountTypeRequest(nameSupplier.get(), null);
        Map.Entry<Long, AccountTypeRequest> child = createAccountTypeRequest(nameSupplier.get(), parent.getKey());

        AccountType result = integrationTestHelper.fetchById(baseUrl, child.getKey(), emptyMap(), AccountType.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenFetchNonExistentById_thenReturnHttp404() throws Exception {
        Map.Entry<Long, AccountTypeRequest> parent = createAccountTypeRequest(nameSupplier.get(), null);
        Map.Entry<Long, AccountTypeRequest> child = createAccountTypeRequest(nameSupplier.get(), parent.getKey());

        AccountType result = integrationTestHelper.fetchById(baseUrl, child.getKey() + 1, emptyMap(), AccountType.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetch_thenReturnHttp200() throws Exception {
        Collection<AccountType> result = integrationTestHelper.fetch(baseUrl, emptyMap(), AccountType.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, AccountTypeRequest> parent = createAccountTypeRequest(nameSupplier.get(), null);
        Map.Entry<Long, AccountTypeRequest> child = createAccountTypeRequest(nameSupplier.get(), parent.getKey());

        Long result = integrationTestHelper.update(baseUrl, child.getKey(), child.getValue(), Long.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, AccountTypeRequest> parent = createAccountTypeRequest(nameSupplier.get(), null);
        Map.Entry<Long, AccountTypeRequest> child = createAccountTypeRequest(nameSupplier.get(), parent.getKey());

        Long result = integrationTestHelper.update(baseUrl, child.getKey() + 1, child.getValue(), Long.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, AccountTypeRequest> parent = createAccountTypeRequest(nameSupplier.get(), null);
        Map.Entry<Long, AccountTypeRequest> child = createAccountTypeRequest(nameSupplier.get(), parent.getKey());

        Void result = integrationTestHelper.delete(baseUrl, child.getKey(), Void.class, OK);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, AccountTypeRequest> parent = createAccountTypeRequest(nameSupplier.get(), null);
        Map.Entry<Long, AccountTypeRequest> child = createAccountTypeRequest(nameSupplier.get(), parent.getKey());

        Void result = integrationTestHelper.delete(baseUrl, child.getKey() + 1, Void.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    private Map.Entry<Long, AccountTypeRequest> createAccountTypeRequest(String name, Long parentId) throws Exception {
        AccountTypeRequest accountTypeRequest = new AccountTypeRequest(name, "Description for " + name, parentId);
        Long result = integrationTestHelper.create(baseUrl, accountTypeRequest, Long.class, CREATED);
        Assertions.assertNotNull(result);
        return Map.entry(result, accountTypeRequest);
    }
}
