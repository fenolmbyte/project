package ru.tinkoff.edu.java.scrapper.service.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.client.BotWebClient;
import ru.tinkoff.edu.java.scrapper.dto.bot.LinkUpdateRequest;

@Service
@RequiredArgsConstructor
public class BotWebService {
    private final BotWebClient webClient;

    public void sendUpdate(LinkUpdateRequest request) {
        webClient.sendUpdates(request);
    }
}
