package ru.tinkoff.edu.java.scrapper.service.client;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.client.GitHubWebClient;
import ru.tinkoff.edu.java.scrapper.dto.client.GitHubEventResponse;
import ru.tinkoff.edu.java.scrapper.dto.client.UpdatesInfo;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubWebService {
    private final GitHubWebClient gitHubWebClient;

    public @Nullable UpdatesInfo fetchEventsUpdates(String owner, String repo, OffsetDateTime lastUpdateTimeSaved) {
        List<GitHubEventResponse> events = gitHubWebClient.fetchEvents(owner, repo).block();
        log.info("Events fetched");
        if (!events.isEmpty()) {
            GitHubEventResponse lastEvent = events.get(0);
            List<String> eventsInfo = events
                    .stream()
                    .filter(event -> event.getCreatedAt().isAfter(lastUpdateTimeSaved))
                    .map(event -> getEventTypeDescription(event.getType()) + " at " + event.getCreatedAt())
                    .toList();
            return new UpdatesInfo(lastEvent.getCreatedAt(), eventsInfo);
        }
        return null;
    }

    private String getEventTypeDescription(String eventType) {
        try {
            return GitHubEventType.valueOf(eventType).getDescription();
        } catch (IllegalArgumentException ignored) {
            return GitHubEventType.UnknownEvent.getDescription();
        }
    }
}
