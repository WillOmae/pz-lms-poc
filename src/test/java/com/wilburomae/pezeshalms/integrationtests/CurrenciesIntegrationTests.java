package com.wilburomae.pezeshalms.integrationtests;

import com.wilburomae.pezeshalms.accounts.dtos.Currency;
import com.wilburomae.pezeshalms.accounts.dtos.CurrencyRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

    private final String baseUrl = "/accounts/currencies";
    private final Supplier<String> nameSupplier = () -> "CURRENCY_" + System.nanoTime() + RANDOM.nextInt(100, 1000);

    @Test
    void whenCreateNew_thenReturnHttp201() throws Exception {
        createCurrencyRequest(nameSupplier.get());
    }

    @Test
    void whenCreateDuplicate_thenReturnHttp409() throws Exception {
        Map.Entry<Long, CurrencyRequest> created = createCurrencyRequest(nameSupplier.get());

        Long result = integrationTestHelper.create(baseUrl, created.getValue(), Long.class, CONFLICT);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetchExistingById_thenReturnHttp200() throws Exception {
        Map.Entry<Long, CurrencyRequest> created = createCurrencyRequest(nameSupplier.get());

        Currency result = integrationTestHelper.fetchById(baseUrl, created.getKey(), emptyMap(), Currency.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenFetchNonExistentById_thenReturnHttp404() throws Exception {
        Map.Entry<Long, CurrencyRequest> created = createCurrencyRequest(nameSupplier.get());

        Currency result = integrationTestHelper.fetchById(baseUrl, created.getKey() + 1, emptyMap(), Currency.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetch_thenReturnHttp200() throws Exception {
        Collection<Currency> result = integrationTestHelper.fetch(baseUrl, emptyMap(), Currency.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, CurrencyRequest> created = createCurrencyRequest(nameSupplier.get());

        Long result = integrationTestHelper.update(baseUrl, created.getKey(), created.getValue(), Long.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, CurrencyRequest> created = createCurrencyRequest(nameSupplier.get());

        Long result = integrationTestHelper.update(baseUrl, created.getKey() + 1, created.getValue(), Long.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, CurrencyRequest> created = createCurrencyRequest(nameSupplier.get());

        Void result = integrationTestHelper.delete(baseUrl, created.getKey(), Void.class, OK);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, CurrencyRequest> created = createCurrencyRequest(nameSupplier.get());

        Void result = integrationTestHelper.delete(baseUrl, created.getKey() + 1, Void.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    private Map.Entry<Long, CurrencyRequest> createCurrencyRequest(String name) throws Exception {
        String code = fixedRandom(UPPER, 3);
        String numericCode = fixedRandom(DIGITS, 3);
        CurrencyRequest currencyRequest = new CurrencyRequest(code, numericCode, "null", name, (short) 2, true);
        Long result = integrationTestHelper.create(baseUrl, currencyRequest, Long.class, CREATED);
        Assertions.assertNotNull(result);
        return Map.entry(result, currencyRequest);
    }
}
