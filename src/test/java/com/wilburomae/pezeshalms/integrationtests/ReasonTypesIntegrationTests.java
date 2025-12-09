package com.wilburomae.pezeshalms.integrationtests;

import com.wilburomae.pezeshalms.helpers.IntegrationTestHelper;
import com.wilburomae.pezeshalms.integrationtests.TransactionTypesIntegrationTests.TransactionTypeGenerator;
import com.wilburomae.pezeshalms.transactions.dtos.ReasonType;
import com.wilburomae.pezeshalms.transactions.dtos.ReasonTypeRequest;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionTypeRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Supplier;

import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.*;

public class ReasonTypesIntegrationTests extends BaseIntegrationTests {

    private static final Random RANDOM = new SecureRandom();
    private static final String BASE_URL = "/transactions/reasons";

    private ReasonTypeGenerator reasonTypeGenerator;

    @BeforeEach
    void setup() throws Exception {
        if (reasonTypeGenerator == null) {
            reasonTypeGenerator = new ReasonTypeGenerator(integrationTestHelper);
        }
    }

    @Test
    void whenCreateNew_thenReturnHttp201() throws Exception {
        reasonTypeGenerator.createRequest();
    }

    @Test
    void whenCreateDuplicate_thenReturnHttp409() throws Exception {
        Map.Entry<Long, ReasonTypeRequest> created = reasonTypeGenerator.createRequest();

        Long result = integrationTestHelper.create(BASE_URL, created.getValue(), Long.class, CONFLICT);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetchExistingById_thenReturnHttp200() throws Exception {
        Map.Entry<Long, ReasonTypeRequest> created = reasonTypeGenerator.createRequest();

        ReasonType result = integrationTestHelper.fetchById(BASE_URL, created.getKey(), emptyMap(), ReasonType.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenFetchNonExistentById_thenReturnHttp404() throws Exception {
        Map.Entry<Long, ReasonTypeRequest> created = reasonTypeGenerator.createRequest();

        ReasonType result = integrationTestHelper.fetchById(BASE_URL, created.getKey() + 1, emptyMap(), ReasonType.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetch_thenReturnHttp200() throws Exception {
        Collection<ReasonType> result = integrationTestHelper.fetch(BASE_URL, emptyMap(), ReasonType.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, ReasonTypeRequest> created = reasonTypeGenerator.createRequest();

        Long result = integrationTestHelper.update(BASE_URL, created.getKey() + 1, created.getValue(), Long.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, ReasonTypeRequest> created = reasonTypeGenerator.createRequest();

        Void result = integrationTestHelper.delete(BASE_URL, created.getKey(), Void.class, OK);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, ReasonTypeRequest> created = reasonTypeGenerator.createRequest();

        Void result = integrationTestHelper.delete(BASE_URL, created.getKey() + 1, Void.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    public static class ReasonTypeGenerator {

        private final IntegrationTestHelper integrationTestHelper;
        private final List<Long> transactionTypes = new ArrayList<>();
        private final Supplier<String> nameSupplier = () -> "REASON_TYPE_" + System.nanoTime() + RANDOM.nextInt(100, 1000);

        public ReasonTypeGenerator(IntegrationTestHelper integrationTestHelper) throws Exception {
            this.integrationTestHelper = integrationTestHelper;

            TransactionTypeGenerator transactionTypeGenerator = new TransactionTypeGenerator(integrationTestHelper);
            for (int i = 0; i < 10; i++) {
                Map.Entry<Long, TransactionTypeRequest> created = transactionTypeGenerator.createRequest();
                transactionTypes.add(created.getKey());
            }
        }

        public Map.Entry<Long, ReasonTypeRequest> createRequest() throws Exception {
            String name = nameSupplier.get();
            ReasonTypeRequest request = new ReasonTypeRequest(name, "Description for " + name + ".", transactionTypes);
            Long result = integrationTestHelper.create(BASE_URL, request, Long.class, CREATED);
            Assertions.assertNotNull(result);
            return Map.entry(result, request);
        }
    }
}
