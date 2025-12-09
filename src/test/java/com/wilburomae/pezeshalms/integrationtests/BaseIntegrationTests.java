package com.wilburomae.pezeshalms.integrationtests;

import com.wilburomae.pezeshalms.helpers.DbHelper;
import com.wilburomae.pezeshalms.helpers.IntegrationTestHelper;
import com.wilburomae.pezeshalms.helpers.TestcontainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class BaseIntegrationTests {

    @Autowired
    IntegrationTestHelper integrationTestHelper;

    @Autowired
    DbHelper dbHelper;

    @BeforeEach
    void initDb() {
        dbHelper.cleanDatabase();
    }
}
