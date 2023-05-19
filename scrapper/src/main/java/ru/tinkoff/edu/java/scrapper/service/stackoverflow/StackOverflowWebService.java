package ru.tinkoff.edu.java.scrapper.service.stackoverflow;

import jakarta.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowWebClient;
import ru.tinkoff.edu.java.scrapper.dto.client.StackOverflowQuestionResponse;
import ru.tinkoff.edu.java.scrapper.dto.client.StackOverflowQuestionsResponse;
import ru.tinkoff.edu.java.scrapper.dto.client.UpdatesInfo;

@Service
@RequiredArgsConstructor
public class StackOverflowWebService {
    private final StackOverflowWebClient stackOverflowWebClient;

    public @Nullable UpdatesInfo fetchQuestionUpdates(Integer id) {
        StackOverflowQuestionsResponse response = stackOverflowWebClient.fetchQuestion(id).block();
        if (!response.items().isEmpty()) {
            StackOverflowQuestionResponse questionResponse = response.items().get(0);
            return new UpdatesInfo(
                questionResponse.lastActivityDate(),
                List.of("Check out new update from " + questionResponse.title())
            );
        }
        return null;
    }

    public List<StackOverflowQuestionResponse> fetchQuestions(List<Integer> ids) {
        return stackOverflowWebClient
            .fetchQuestions(ids
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(";")))
            .map(StackOverflowQuestionsResponse::items).block();
    }
}
