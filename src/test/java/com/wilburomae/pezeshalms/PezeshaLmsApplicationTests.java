package com.wilburomae.pezeshalms;

import com.wilburomae.pezeshalms.integrationtests.BaseIntegrationTests;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PezeshaLmsApplicationTests extends BaseIntegrationTests {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void contextLoads() {
    }

    @Test
    void testDbInitialization() {
        assertDoesNotThrow(() -> applicationContext.getBeansOfType(JpaRepository.class)
                .forEach((repositoryName, repository) -> {
                    long count = repository.count();
                    if (repositoryName.equals("permissionRepository")) {
                        assertTrue(count > 0);
                    }
                    logger.info("Count of {} is {}", repositoryName, count);
                }));
    }
}
