package ru.tinkoff.edu.java.bot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.controller.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.telegram.TrackerBot;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class UpdatesController {
    private final TrackerBot bot;

    @PostMapping(
        path = "/updates",
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void sendUpdates(@RequestBody LinkUpdateRequest request) {
        log.info(request.toString());
        bot.sendUpdates(request);
    }
}
