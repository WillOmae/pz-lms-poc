package com.wilburomae.pezeshalms.integrationtests;

import com.wilburomae.pezeshalms.accounts.dtos.AccountType;
import com.wilburomae.pezeshalms.accounts.dtos.AccountTypeRequest;
import com.wilburomae.pezeshalms.helpers.IntegrationTestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    private static final String BASE_URL = "/accounts/types";

    private AccountTypeGenerator accountTypeGenerator;

    @BeforeEach
    public void setUp() {
        if (accountTypeGenerator == null) {
            accountTypeGenerator = new AccountTypeGenerator(integrationTestHelper);
        }
    }

    @Test
    void whenCreateNew_thenReturnHttp201() throws Exception {
        Map.Entry<Long, AccountTypeRequest> parent = accountTypeGenerator.createRequest(null);
        accountTypeGenerator.createRequest(parent.getKey());
    }

    @Test
    void whenCreateDuplicate_thenReturnHttp409() throws Exception {
        Map.Entry<Long, AccountTypeRequest> parent = accountTypeGenerator.createRequest(null);
        Map.Entry<Long, AccountTypeRequest> child = accountTypeGenerator.createRequest(parent.getKey());

        Long result = integrationTestHelper.create(BASE_URL, child.getValue(), Long.class, CONFLICT);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetchExistingById_thenReturnHttp200() throws Exception {
        Map.Entry<Long, AccountTypeRequest> parent = accountTypeGenerator.createRequest(null);
        Map.Entry<Long, AccountTypeRequest> child = accountTypeGenerator.createRequest(parent.getKey());

        AccountType result = integrationTestHelper.fetchById(BASE_URL, child.getKey(), emptyMap(), AccountType.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenFetchNonExistentById_thenReturnHttp404() throws Exception {
        Map.Entry<Long, AccountTypeRequest> parent = accountTypeGenerator.createRequest(null);
        Map.Entry<Long, AccountTypeRequest> child = accountTypeGenerator.createRequest(parent.getKey());

        AccountType result = integrationTestHelper.fetchById(BASE_URL, child.getKey() + 1, emptyMap(), AccountType.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetch_thenReturnHttp200() throws Exception {
        Collection<AccountType> result = integrationTestHelper.fetch(BASE_URL, emptyMap(), AccountType.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, AccountTypeRequest> parent = accountTypeGenerator.createRequest(null);
        Map.Entry<Long, AccountTypeRequest> child = accountTypeGenerator.createRequest(parent.getKey());

        Long result = integrationTestHelper.update(BASE_URL, child.getKey(), child.getValue(), Long.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, AccountTypeRequest> parent = accountTypeGenerator.createRequest(null);
        Map.Entry<Long, AccountTypeRequest> child = accountTypeGenerator.createRequest(parent.getKey());

        Long result = integrationTestHelper.update(BASE_URL, child.getKey() + 1, child.getValue(), Long.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, AccountTypeRequest> parent = accountTypeGenerator.createRequest(null);
        Map.Entry<Long, AccountTypeRequest> child = accountTypeGenerator.createRequest(parent.getKey());

        Void result = integrationTestHelper.delete(BASE_URL, child.getKey(), Void.class, OK);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, AccountTypeRequest> parent = accountTypeGenerator.createRequest(null);
        Map.Entry<Long, AccountTypeRequest> child = accountTypeGenerator.createRequest(parent.getKey());

        Void result = integrationTestHelper.delete(BASE_URL, child.getKey() + 1, Void.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    public static class AccountTypeGenerator {

        private final IntegrationTestHelper integrationTestHelper;
        private final Supplier<String> nameSupplier = () -> "ACCOUNT_TYPE_" + System.nanoTime() + RANDOM.nextInt(100, 1000);

        public AccountTypeGenerator(IntegrationTestHelper integrationTestHelper) {
            this.integrationTestHelper = integrationTestHelper;
        }

        public Map.Entry<Long, AccountTypeRequest> createRequest(Long parentId) throws Exception {
            String name = nameSupplier.get();
            AccountTypeRequest request = new AccountTypeRequest(name, "Description for " + name, parentId);
            Long result = integrationTestHelper.create(BASE_URL, request, Long.class, CREATED);
            Assertions.assertNotNull(result);
            return Map.entry(result, request);
        }
    }
}
