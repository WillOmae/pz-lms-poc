package com.wilburomae.pezeshalms.integrationtests;

import com.wilburomae.pezeshalms.accounts.dtos.AccountRequest;
import com.wilburomae.pezeshalms.helpers.IntegrationTestHelper;
import com.wilburomae.pezeshalms.integrationtests.AccountsIntegrationTests.AccountGenerator;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionType;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionTypeComponentRequest;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionTypeRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Supplier;

import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.*;

public class TransactionTypesIntegrationTests extends BaseIntegrationTests {

    private static final Random RANDOM = new SecureRandom();
    private static final String BASE_URL = "/transactions/types";

    private TransactionTypeGenerator transactionTypeGenerator;

    @BeforeEach
    void setup() throws Exception {
        if (transactionTypeGenerator == null) {
            transactionTypeGenerator = new TransactionTypeGenerator(integrationTestHelper);
        }
    }

    @Test
    void whenCreateNew_thenReturnHttp201() throws Exception {
        transactionTypeGenerator.createRequest();
    }

    @Test
    void whenCreateDuplicate_thenReturnHttp409() throws Exception {
        Map.Entry<Long, TransactionTypeRequest> created = transactionTypeGenerator.createRequest();

        Long result = integrationTestHelper.create(BASE_URL, created.getValue(), Long.class, CONFLICT);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetchExistingById_thenReturnHttp200() throws Exception {
        Map.Entry<Long, TransactionTypeRequest> created = transactionTypeGenerator.createRequest();

        TransactionType result = integrationTestHelper.fetchById(BASE_URL, created.getKey(), emptyMap(), TransactionType.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenFetchNonExistentById_thenReturnHttp404() throws Exception {
        Map.Entry<Long, TransactionTypeRequest> created = transactionTypeGenerator.createRequest();

        TransactionType result = integrationTestHelper.fetchById(BASE_URL, created.getKey() + 1, emptyMap(), TransactionType.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetch_thenReturnHttp200() throws Exception {
        Collection<TransactionType> result = integrationTestHelper.fetch(BASE_URL, emptyMap(), TransactionType.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, TransactionTypeRequest> created = transactionTypeGenerator.createRequest();

        Long result = integrationTestHelper.update(BASE_URL, created.getKey() + 1, created.getValue(), Long.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, TransactionTypeRequest> created = transactionTypeGenerator.createRequest();

        Void result = integrationTestHelper.delete(BASE_URL, created.getKey(), Void.class, OK);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, TransactionTypeRequest> created = transactionTypeGenerator.createRequest();

        Void result = integrationTestHelper.delete(BASE_URL, created.getKey() + 1, Void.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    public static class TransactionTypeGenerator {

        private final IntegrationTestHelper integrationTestHelper;
        private final List<Long> accounts = new ArrayList<>();
        private final Supplier<String> nameSupplier = () -> "TRANSACTION_TYPE_" + System.nanoTime() + RANDOM.nextInt(100, 1000);
        private final Supplier<String> balanceNameSupplier = () -> "TRANSACTION_TYPE_COMPONENT_" + System.nanoTime() + RANDOM.nextInt(100, 1000);

        public TransactionTypeGenerator(IntegrationTestHelper integrationTestHelper) throws Exception {
            this.integrationTestHelper = integrationTestHelper;

            AccountGenerator accountGenerator = new AccountGenerator(integrationTestHelper);
            for (int i = 0; i < 10; i++) {
                Map.Entry<Long, AccountRequest> created = accountGenerator.createRequest();
                accounts.add(created.getKey());
            }
        }

        public Map.Entry<Long, TransactionTypeRequest> createRequest() throws Exception {
            String name = nameSupplier.get();
            List<TransactionTypeComponentRequest> components = new ArrayList<>();
            for (int i = 0; i < accounts.size(); i += 2) {
                long debitAccountId = accounts.get(i);
                long creditAccountId = accounts.get(i + 1);
                String balanceName = balanceNameSupplier.get();
                TransactionTypeComponentRequest balance = new TransactionTypeComponentRequest(null, balanceName, "Description for " + balanceName + ".", (short) 1, debitAccountId, creditAccountId);
                components.add(balance);
            }

            TransactionTypeRequest request = new TransactionTypeRequest(name, "Description for " + name + ".", false, components);
            Long result = integrationTestHelper.create(BASE_URL, request, Long.class, CREATED);
            Assertions.assertNotNull(result);
            return Map.entry(result, request);
        }
    }
}
