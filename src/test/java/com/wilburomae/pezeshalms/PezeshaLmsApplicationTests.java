package com.wilburomae.pezeshalms;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class PezeshaLmsApplicationTests {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void contextLoads() {
    }

    @Test
    void testDbInitialization() {
        Assertions.assertDoesNotThrow(() -> {
            Map<String, JpaRepository> repositories = applicationContext.getBeansOfType(JpaRepository.class);
            for (var kv : repositories.entrySet()) {
                String repositoryName = kv.getKey();
                JpaRepository repository = kv.getValue();
                long count = repository.count();
                logger.info("Count of {} is {}", repositoryName, count);
            }
        });
    }
}
