package com.wilburomae.pezeshalms.integrationtests;

import com.wilburomae.pezeshalms.helpers.IntegrationTestHelper;
import com.wilburomae.pezeshalms.helpers.TestcontainersConfiguration;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public abstract class BaseIntegrationTests {

    @Autowired
    IntegrationTestHelper integrationTestHelper;
}
