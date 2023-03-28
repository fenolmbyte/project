package ru.tinkoff.edu.java.scrapper.client.github;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubRepositoryResponse;

@HttpExchange(
        accept = MediaType.APPLICATION_JSON_VALUE,
        contentType = MediaType.APPLICATION_JSON_VALUE
)
public interface GitHubWebClient {
    @GetExchange("/repos/{owner}/{repo}")
    Mono<GitHubRepositoryResponse> fetchRepo(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo
    );
}
