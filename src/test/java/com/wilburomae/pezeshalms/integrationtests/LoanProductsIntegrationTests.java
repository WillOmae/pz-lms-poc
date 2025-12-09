package com.wilburomae.pezeshalms.integrationtests;

import com.wilburomae.pezeshalms.helpers.IntegrationTestHelper;
import com.wilburomae.pezeshalms.integrationtests.ReasonTypesIntegrationTests.ReasonTypeGenerator;
import com.wilburomae.pezeshalms.integrationtests.UsersIntegrationTests.UserGenerator;
import com.wilburomae.pezeshalms.products.dtos.LoanProduct;
import com.wilburomae.pezeshalms.products.dtos.LoanProductRequest;
import com.wilburomae.pezeshalms.transactions.dtos.ReasonTypeRequest;
import com.wilburomae.pezeshalms.users.data.repositories.RoleRepository;
import com.wilburomae.pezeshalms.users.dtos.UserRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Supplier;

import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.*;

public class LoanProductsIntegrationTests extends BaseIntegrationTests {

    private static final Random RANDOM = new SecureRandom();
    private static final String BASE_URL = "/products";
    @Autowired
    RoleRepository roleRepository;
    private LoanProductGenerator loanProductGenerator;

    @BeforeEach
    void setup() throws Exception {
        if (loanProductGenerator == null) {
            loanProductGenerator = new LoanProductGenerator(integrationTestHelper, roleRepository);
        }
    }

    @Test
    void whenCreateNew_thenReturnHttp201() throws Exception {
        loanProductGenerator.createRequest();
    }

    @Test
    void whenCreateDuplicate_thenReturnHttp409() throws Exception {
        Map.Entry<Long, LoanProductRequest> created = loanProductGenerator.createRequest();

        Long result = integrationTestHelper.create(BASE_URL, created.getValue(), Long.class, CONFLICT);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetchExistingById_thenReturnHttp200() throws Exception {
        Map.Entry<Long, LoanProductRequest> created = loanProductGenerator.createRequest();

        LoanProduct result = integrationTestHelper.fetchById(BASE_URL, created.getKey(), emptyMap(), LoanProduct.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenFetchNonExistentById_thenReturnHttp404() throws Exception {
        Map.Entry<Long, LoanProductRequest> created = loanProductGenerator.createRequest();

        LoanProduct result = integrationTestHelper.fetchById(BASE_URL, created.getKey() + 1, emptyMap(), LoanProduct.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetch_thenReturnHttp200() throws Exception {
        Collection<LoanProduct> result = integrationTestHelper.fetch(BASE_URL, emptyMap(), LoanProduct.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenUpdateNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, LoanProductRequest> created = loanProductGenerator.createRequest();

        Long result = integrationTestHelper.update(BASE_URL, created.getKey() + 1, created.getValue(), Long.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, LoanProductRequest> created = loanProductGenerator.createRequest();

        Void result = integrationTestHelper.delete(BASE_URL, created.getKey(), Void.class, OK);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, LoanProductRequest> created = loanProductGenerator.createRequest();

        Void result = integrationTestHelper.delete(BASE_URL, created.getKey() + 1, Void.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    public static class LoanProductGenerator {

        private final IntegrationTestHelper integrationTestHelper;
        private final List<Long> reasonTypes = new ArrayList<>();
        private final List<Long> partners = new ArrayList<>();
        private final Supplier<String> nameSupplier = () -> "LOAN_PRODUCT_" + System.nanoTime() + RANDOM.nextInt(100, 1000);

        public LoanProductGenerator(IntegrationTestHelper integrationTestHelper, RoleRepository roleRepository) throws Exception {
            this.integrationTestHelper = integrationTestHelper;

            ReasonTypeGenerator reasonTypeGenerator = new ReasonTypeGenerator(integrationTestHelper);
            for (int i = 0; i < 10; i++) {
                Map.Entry<Long, ReasonTypeRequest> created = reasonTypeGenerator.createRequest();
                reasonTypes.add(created.getKey());
            }

            UserGenerator userGenerator = new UserGenerator(integrationTestHelper, roleRepository);
            for (int i = 0; i < 10; i++) {
                Map.Entry<Long, UserRequest> created = userGenerator.createRequest("PARTNER");
                partners.add(created.getKey());
            }
        }

        public Map.Entry<Long, LoanProductRequest> createRequest() throws Exception {
            String name = nameSupplier.get();
            LoanProductRequest request = new LoanProductRequest(name, "Description for " + name + ".", true, new BigDecimal("0.25"), true, new BigDecimal("3"), 30, "D", "D", 2, true, partners, reasonTypes);
            Long result = integrationTestHelper.create(BASE_URL, request, Long.class, CREATED);
            Assertions.assertNotNull(result);
            return Map.entry(result, request);
        }
    }
}
