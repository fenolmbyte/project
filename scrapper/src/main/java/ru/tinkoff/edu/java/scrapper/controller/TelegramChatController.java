package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequestMapping("/tg-chat")
@RestController
public class TelegramChatController {
    @PostMapping(
            path = "/{id}"
    )
    public void create(@PathVariable("id") Long id) {
        // TODO: implement
    }

    @DeleteMapping(
            path = "/{id}"
    )
    public void delete(@PathVariable("id") Long id) {
        // TODO: implement
    }
}
