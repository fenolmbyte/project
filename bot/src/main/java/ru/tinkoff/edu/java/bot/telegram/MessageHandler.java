package ru.tinkoff.edu.java.bot.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tinkoff.edu.java.bot.telegram.command.Command;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageHandler {
    private final List<Command> commands;

    public SendMessage handle(Message message) {
        return commands.stream()
                .filter(command -> command.supports(message))
                .map(command -> command.handle(message))
                .findFirst()
                .get();
    }
}
