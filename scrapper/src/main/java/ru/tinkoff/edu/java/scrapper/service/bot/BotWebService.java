package ru.tinkoff.edu.java.scrapper.service.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.client.BotWebClient;
import ru.tinkoff.edu.java.scrapper.dto.bot.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.service.UpdatesSender;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false", matchIfMissing = true)
public class BotWebService implements UpdatesSender {
    private final BotWebClient webClient;

    public void sendUpdate(LinkUpdateRequest request) {
        webClient.sendUpdates(request);
    }
}
