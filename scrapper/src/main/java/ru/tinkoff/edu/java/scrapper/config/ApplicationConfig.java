package ru.tinkoff.edu.java.scrapper.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.parser.ParserConfig;

@EnableScheduling
@Validated
@Data
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@Configuration
@Import(value = {ParserConfig.class})
public class ApplicationConfig {
    @NotNull
    private Scheduler scheduler;
    @NotNull
    private GitHub gitHub;
    @NotNull
    private StackOverflow stackOverflow;
    @NotNull
    private Bot bot;
    @NotNull
    private AccessType databaseAccessType;
    @NotNull
    private RabbitQueue rabbitQueue;

    private Boolean useQueue = false;

    @Validated
    @Data
    public static class Scheduler {
        @NotNull
        private Duration interval;
        @NotNull
        private Duration linkToBeCheckedInterval;
    }

    @Validated
    @Data
    public static class GitHub {
        @NotBlank
        private String url = "https://api.github.com";
    }

    @Validated
    @Data
    public static class StackOverflow {
        @NotBlank
        private String url = "https://stackoverflow.com/2.3";
    }

    @Validated
    @Data
    public static class Bot {
        @NotBlank
        private String url;
    }

    @Validated
    @Data
    public static class RabbitQueue {
        @NotBlank
        private String exchangeName;
        @NotBlank
        private String queueName;
        @NotBlank
        private String routingKey;
    }

    public enum AccessType {
        JDBC,
        JPA,
        JOOQ
    }
}
