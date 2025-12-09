package com.wilburomae.pezeshalms.integrationtests;

import com.wilburomae.pezeshalms.accounts.dtos.Currency;
import com.wilburomae.pezeshalms.accounts.dtos.CurrencyRequest;
import com.wilburomae.pezeshalms.helpers.HttpTestHelper;
import com.wilburomae.pezeshalms.helpers.IntegrationTestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import static com.wilburomae.pezeshalms.common.utilities.StringUtilities.*;
import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.*;

public class CurrenciesIntegrationTests extends BaseIntegrationTests {

    private static final Random RANDOM = new SecureRandom();
    private static final String BASE_URL = "/accounts/currencies";

    private CurrencyGenerator currencyGenerator;

    @BeforeEach
    void setUp() {
        if (currencyGenerator == null) {
            currencyGenerator = new CurrencyGenerator(integrationTestHelper);
        }
    }

    @Test
    void whenCreateNew_thenReturnHttp201() throws Exception {
        currencyGenerator.createRequest();
    }

    @Test
    void whenCreateDuplicate_thenReturnHttp409() throws Exception {
        Map.Entry<Long, CurrencyRequest> created = currencyGenerator.createRequest();

        Long result = integrationTestHelper.create(BASE_URL, created.getValue(), Long.class, CONFLICT);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetchExistingById_thenReturnHttp200() throws Exception {
        Map.Entry<Long, CurrencyRequest> created = currencyGenerator.createRequest();

        Currency result = integrationTestHelper.fetchById(BASE_URL, created.getKey(), emptyMap(), Currency.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenFetchNonExistentById_thenReturnHttp404() throws Exception {
        Map.Entry<Long, CurrencyRequest> created = currencyGenerator.createRequest();

        Currency result = integrationTestHelper.fetchById(BASE_URL, created.getKey() + 1, emptyMap(), Currency.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetch_thenReturnHttp200() throws Exception {
        Collection<Currency> result = integrationTestHelper.fetch(BASE_URL, emptyMap(), Currency.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, CurrencyRequest> created = currencyGenerator.createRequest();

        Long result = integrationTestHelper.update(BASE_URL, created.getKey(), created.getValue(), Long.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, CurrencyRequest> created = currencyGenerator.createRequest();

        Long result = integrationTestHelper.update(BASE_URL, created.getKey() + 1, created.getValue(), Long.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, CurrencyRequest> created = currencyGenerator.createRequest();

        Void result = integrationTestHelper.delete(BASE_URL, created.getKey(), Void.class, OK);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, CurrencyRequest> created = currencyGenerator.createRequest();

        Void result = integrationTestHelper.delete(BASE_URL, created.getKey() + 1, Void.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    public static class CurrencyGenerator {

        private final IntegrationTestHelper integrationTestHelper;
        private final Supplier<String> nameSupplier = () -> "CURRENCY_" + System.nanoTime() + RANDOM.nextInt(100, 1000);

        public CurrencyGenerator(IntegrationTestHelper integrationTestHelper) {
            this.integrationTestHelper = integrationTestHelper;
        }

        public Map.Entry<Long, CurrencyRequest> createRequest() throws Exception {
            return createRequest(0);
        }

        private Map.Entry<Long, CurrencyRequest> createRequest(int retryCount) throws Exception {
            String name = nameSupplier.get();
            String code = fixedRandom(UPPER, 3);
            String numericCode = fixedRandom(DIGITS, 3);
            CurrencyRequest request = new CurrencyRequest(code, numericCode, name, name, (short) 2, true);
            MvcResult result = integrationTestHelper.createNoAssertion(BASE_URL, request);
            HttpTestHelper httpTestHelper = integrationTestHelper.getHttpTestHelper();
            Long createdId = httpTestHelper.mapJsonDataToClass(result, Long.class);
            MockHttpServletResponse response = result.getResponse();
            if (response.getStatus() == HttpStatus.CONFLICT.value()) {
                if (retryCount == 3) {
                    throw new RuntimeException("Unable to create currency");
                }
                return createRequest(++retryCount);
            }
            Assertions.assertNotNull(createdId);
            return Map.entry(createdId, request);
        }
    }
}
