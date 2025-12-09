package com.wilburomae.pezeshalms.integrationtests;

import com.wilburomae.pezeshalms.accounts.dtos.AccountStatus;
import com.wilburomae.pezeshalms.accounts.dtos.AccountStatusRequest;
import org.junit.jupiter.api.Assertions;
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

    private final String baseUrl = "/accounts/statuses";
    private final Supplier<String> nameSupplier = () -> "ACCOUNT_STATUS_" + System.nanoTime() + RANDOM.nextInt(100, 1000);

    @Test
    void whenCreateNew_thenReturnHttp201() throws Exception {
        createRequest(nameSupplier.get());
    }

    @Test
    void whenCreateDuplicate_thenReturnHttp409() throws Exception {
        Map.Entry<Long, AccountStatusRequest> created = createRequest(nameSupplier.get());

        Long result = integrationTestHelper.create(baseUrl, created.getValue(), Long.class, CONFLICT);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetchExistingById_thenReturnHttp200() throws Exception {
        Map.Entry<Long, AccountStatusRequest> created = createRequest(nameSupplier.get());

        AccountStatus result = integrationTestHelper.fetchById(baseUrl, created.getKey(), emptyMap(), AccountStatus.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenFetchNonExistentById_thenReturnHttp404() throws Exception {
        Map.Entry<Long, AccountStatusRequest> created = createRequest(nameSupplier.get());

        AccountStatus result = integrationTestHelper.fetchById(baseUrl, created.getKey() + 1, emptyMap(), AccountStatus.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetch_thenReturnHttp200() throws Exception {
        Collection<AccountStatus> result = integrationTestHelper.fetch(baseUrl, emptyMap(), AccountStatus.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, AccountStatusRequest> created = createRequest(nameSupplier.get());

        Long result = integrationTestHelper.update(baseUrl, created.getKey(), created.getValue(), Long.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, AccountStatusRequest> created = createRequest(nameSupplier.get());

        Long result = integrationTestHelper.update(baseUrl, created.getKey() + 1, created.getValue(), Long.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, AccountStatusRequest> created = createRequest(nameSupplier.get());

        Void result = integrationTestHelper.delete(baseUrl, created.getKey(), Void.class, OK);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, AccountStatusRequest> created = createRequest(nameSupplier.get());

        Void result = integrationTestHelper.delete(baseUrl, created.getKey() + 1, Void.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    private Map.Entry<Long, AccountStatusRequest> createRequest(String name) throws Exception {
        AccountStatusRequest request = new AccountStatusRequest(name, "Description for " + name);
        Long result = integrationTestHelper.create(baseUrl, request, Long.class, CREATED);
        Assertions.assertNotNull(result);
        return Map.entry(result, request);
    }
}
