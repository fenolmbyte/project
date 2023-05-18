package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import ru.tinkoff.edu.java.scrapper.dto.bot.LinkUpdateRequest;

@HttpExchange(
    accept = MediaType.APPLICATION_JSON_VALUE,
    contentType = MediaType.APPLICATION_JSON_VALUE
)
public interface BotWebClient {
    @PostExchange("/updates")
    void sendUpdates(@RequestBody LinkUpdateRequest request);
}
