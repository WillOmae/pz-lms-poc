package com.wilburomae.pezeshalms.integrationtests;

import com.wilburomae.pezeshalms.accounts.dtos.*;
import com.wilburomae.pezeshalms.helpers.IntegrationTestHelper;
import com.wilburomae.pezeshalms.integrationtests.AccountStatusesIntegrationTests.AccountStatusGenerator;
import com.wilburomae.pezeshalms.integrationtests.AccountTypesIntegrationTests.AccountTypeGenerator;
import com.wilburomae.pezeshalms.integrationtests.CurrenciesIntegrationTests.CurrencyGenerator;
import com.wilburomae.pezeshalms.users.dtos.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.*;

import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.*;

public class AccountsIntegrationTests extends BaseIntegrationTests {

    private static final Random RANDOM = new SecureRandom();
    private static final String BASE_URL = "/accounts/accounts";

    private AccountGenerator accountGenerator;

    @BeforeEach
    void setup() throws Exception {
        if (accountGenerator == null) {
            accountGenerator = new AccountGenerator(integrationTestHelper);
        }
    }

    @Test
    void whenCreateNew_thenReturnHttp201() throws Exception {
        accountGenerator.createRequest();
    }

    @Test
    void whenFetchExistingById_thenReturnHttp200() throws Exception {
        Map.Entry<Long, AccountRequest> created = accountGenerator.createRequest();

        Role result = integrationTestHelper.fetchById(BASE_URL, created.getKey(), emptyMap(), Role.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenFetchNonExistentById_thenReturnHttp404() throws Exception {
        Map.Entry<Long, AccountRequest> created = accountGenerator.createRequest();

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
        Map.Entry<Long, AccountRequest> created = accountGenerator.createRequest();

        Long result = integrationTestHelper.update(BASE_URL, created.getKey() + 1, created.getValue(), Long.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, AccountRequest> created = accountGenerator.createRequest();

        Void result = integrationTestHelper.delete(BASE_URL, created.getKey(), Void.class, OK);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, AccountRequest> created = accountGenerator.createRequest();

        Void result = integrationTestHelper.delete(BASE_URL, created.getKey() + 1, Void.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    public static class AccountGenerator {

        private final IntegrationTestHelper integrationTestHelper;
        private final List<Long> currencies = new ArrayList<>();
        private final List<Long> types = new ArrayList<>();
        private final List<Long> statuses = new ArrayList<>();

        public AccountGenerator(IntegrationTestHelper integrationTestHelper) throws Exception {
            this.integrationTestHelper = integrationTestHelper;

            CurrencyGenerator currencyGenerator = new CurrencyGenerator(integrationTestHelper);
            for (int i = 0; i < 3; i++) {
                Map.Entry<Long, CurrencyRequest> created = currencyGenerator.createRequest();
                currencies.add(created.getKey());
            }

            AccountTypeGenerator typeGenerator = new AccountTypeGenerator(integrationTestHelper);
            for (int i = 0; i < 5; i++) {
                Map.Entry<Long, AccountTypeRequest> parent = typeGenerator.createRequest(null);
                Map.Entry<Long, AccountTypeRequest> intermediate = typeGenerator.createRequest(parent.getKey());
                Map.Entry<Long, AccountTypeRequest> child = typeGenerator.createRequest(intermediate.getKey());
                types.add(child.getKey());
            }

            AccountStatusGenerator statusGenerator = new AccountStatusGenerator(integrationTestHelper);
            for (int i = 0; i < 5; i++) {
                Map.Entry<Long, AccountStatusRequest> created = statusGenerator.createRequest();
                statuses.add(created.getKey());
            }
        }

        public Map.Entry<Long, AccountRequest> createRequest() throws Exception {
            List<AccountBalanceRequest> balances = new ArrayList<>();
            for (Long currencyId : currencies) {
                AccountBalanceRequest balance = new AccountBalanceRequest(null, currencyId, 0, 1000000L, 100L);
                balances.add(balance);
            }

            long typeId = types.get(RANDOM.nextInt(types.size()));
            long statusId = statuses.get(RANDOM.nextInt(statuses.size()));
            AccountRequest request = new AccountRequest(null, typeId, false, statusId, balances);
            Long result = integrationTestHelper.create(BASE_URL, request, Long.class, CREATED);
            Assertions.assertNotNull(result);
            return Map.entry(result, request);
        }
    }
}
