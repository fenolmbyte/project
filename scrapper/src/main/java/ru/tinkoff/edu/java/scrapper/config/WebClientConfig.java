package ru.tinkoff.edu.java.scrapper.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import ru.tinkoff.edu.java.scrapper.client.GitHubWebClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowWebClient;

@RequiredArgsConstructor
@Configuration
public class WebClientConfig {
    private final ObjectMapper objectMapper;

    private WebClient buildWebClient(String url) {
        ExchangeStrategies strategies = ExchangeStrategies
                .builder()
                .codecs(clientDefaultCodecsConfigurer -> {
                    clientDefaultCodecsConfigurer
                            .defaultCodecs()
                            .jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                    clientDefaultCodecsConfigurer
                            .defaultCodecs()
                            .jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
                }).build();
        return WebClient.builder()
                .exchangeStrategies(strategies)
                .baseUrl(url)
                .build();
    }

    @Bean
    public GitHubWebClient gitHubWebClient(ApplicationConfig config) {
        WebClient webClient = buildWebClient(config.getGitHub().getUrl());
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .build();
        return httpServiceProxyFactory.createClient(GitHubWebClient.class);
    }

    @Bean
    public StackOverflowWebClient stackOverflowWebClient(ApplicationConfig config) {
        WebClient webClient = buildWebClient(config.getStackOverflow().getUrl());
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .build();
        return httpServiceProxyFactory.createClient(StackOverflowWebClient.class);
    }
}
