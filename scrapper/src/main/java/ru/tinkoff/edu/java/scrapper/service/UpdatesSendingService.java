package ru.tinkoff.edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.dto.bot.LinkUpdateRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdatesSendingService {
    private final UpdatesSender sender;

    public void sendUpdate(LinkUpdateRequest update) {
        sender.sendUpdate(update);
    }
}
