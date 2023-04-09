package ru.tinkoff.edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.client.GitHubWebClient;
import ru.tinkoff.edu.java.scrapper.dto.github.GitHubRepositoryResponse;

@RequiredArgsConstructor
@Service
public class GitHubWebService {
    private final GitHubWebClient gitHubWebClient;

    public Mono<GitHubRepositoryResponse> fetchRepository(String owner, String repo) {
        return gitHubWebClient.fetchRepo(owner, repo);
    }
}
