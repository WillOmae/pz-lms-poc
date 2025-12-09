package com.wilburomae.pezeshalms.integrationtests;

import com.wilburomae.pezeshalms.accounts.dtos.AccountStatus;
import com.wilburomae.pezeshalms.accounts.dtos.AccountStatusRequest;
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

public class AccountStatusesIntegrationTests extends BaseIntegrationTests {

    private static final Random RANDOM = new SecureRandom();
    private static final String BASE_URL = "/accounts/statuses";

    private AccountStatusGenerator accountStatusGenerator;

    @BeforeEach
    void setUp() {
        if (accountStatusGenerator == null) {
            accountStatusGenerator = new AccountStatusGenerator(integrationTestHelper);
        }
    }

    @Test
    void whenCreateNew_thenReturnHttp201() throws Exception {
        accountStatusGenerator.createRequest();
    }

    @Test
    void whenCreateDuplicate_thenReturnHttp409() throws Exception {
        Map.Entry<Long, AccountStatusRequest> created = accountStatusGenerator.createRequest();

        Long result = integrationTestHelper.create(BASE_URL, created.getValue(), Long.class, CONFLICT);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetchExistingById_thenReturnHttp200() throws Exception {
        Map.Entry<Long, AccountStatusRequest> created = accountStatusGenerator.createRequest();

        AccountStatus result = integrationTestHelper.fetchById(BASE_URL, created.getKey(), emptyMap(), AccountStatus.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenFetchNonExistentById_thenReturnHttp404() throws Exception {
        Map.Entry<Long, AccountStatusRequest> created = accountStatusGenerator.createRequest();

        AccountStatus result = integrationTestHelper.fetchById(BASE_URL, created.getKey() + 1, emptyMap(), AccountStatus.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetch_thenReturnHttp200() throws Exception {
        Collection<AccountStatus> result = integrationTestHelper.fetch(BASE_URL, emptyMap(), AccountStatus.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, AccountStatusRequest> created = accountStatusGenerator.createRequest();

        Long result = integrationTestHelper.update(BASE_URL, created.getKey(), created.getValue(), Long.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, AccountStatusRequest> created = accountStatusGenerator.createRequest();

        Long result = integrationTestHelper.update(BASE_URL, created.getKey() + 1, created.getValue(), Long.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, AccountStatusRequest> created = accountStatusGenerator.createRequest();

        Void result = integrationTestHelper.delete(BASE_URL, created.getKey(), Void.class, OK);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, AccountStatusRequest> created = accountStatusGenerator.createRequest();

        Void result = integrationTestHelper.delete(BASE_URL, created.getKey() + 1, Void.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    public static class AccountStatusGenerator {

        private final IntegrationTestHelper integrationTestHelper;
        private final Supplier<String> nameSupplier = () -> "ACCOUNT_STATUS_" + System.nanoTime() + RANDOM.nextInt(100, 1000);

        public AccountStatusGenerator(IntegrationTestHelper integrationTestHelper) {
            this.integrationTestHelper = integrationTestHelper;
        }

        public Map.Entry<Long, AccountStatusRequest> createRequest() throws Exception {
            String name = nameSupplier.get();
            AccountStatusRequest request = new AccountStatusRequest(name, "Description for " + name);
            Long result = integrationTestHelper.create(BASE_URL, request, Long.class, CREATED);
            Assertions.assertNotNull(result);
            return Map.entry(result, request);
        }
    }
}
