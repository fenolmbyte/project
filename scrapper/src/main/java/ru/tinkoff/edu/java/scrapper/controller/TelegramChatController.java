package ru.tinkoff.edu.java.scrapper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.service.domain.api.ChatService;

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
