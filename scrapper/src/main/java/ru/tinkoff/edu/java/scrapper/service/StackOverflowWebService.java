package ru.tinkoff.edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowWebClient;
import ru.tinkoff.edu.java.scrapper.dto.stackoverflow.StackOverflowQuestionResponse;
import ru.tinkoff.edu.java.scrapper.dto.stackoverflow.StackOverflowQuestionsResponse;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StackOverflowWebService {
    private final StackOverflowWebClient stackOverflowWebClient;

    public Mono<StackOverflowQuestionResponse> fetchQuestion(Integer id) {
        return stackOverflowWebClient
                .fetchQuestion(id)
                .map(item -> item.items().get(0));
    }

    public Mono<List<StackOverflowQuestionResponse>> fetchQuestions(List<Integer> ids) {
        return stackOverflowWebClient
                .fetchQuestions(ids
                        .stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(";")))
                .map(StackOverflowQuestionsResponse::items);
    }
}
