package ru.tinkoff.edu.java.scrapper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.service.api.ChatService;

@Validated
@RequestMapping("/tg-chat")
@RestController
@RequiredArgsConstructor
public class TelegramChatController {
    private final ChatService chatService;

    @PostMapping(
            path = "/{id}"
    )
    public void create(@PathVariable("id") Long id) {
        chatService.register(id);
    }

    @DeleteMapping(
            path = "/{id}"
    )
    public void delete(@PathVariable("id") Long id) {
        chatService.unregister(id);
    }
}
