package ru.tinkoff.edu.java.scrapper.service.domain.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@Configuration
public class ServicesTestConfig {
    @Bean
    public ServicesTestHelper servicesTestHelper(JdbcTemplate template) {
        return new ServicesTestHelper(template);
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("databaseAccessType", () -> "jpa");
    }
}
