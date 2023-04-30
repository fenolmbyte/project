package ru.tinkoff.edu.java.bot.telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tinkoff.edu.java.bot.service.ScrapperWebService;

@Order(6)
@Component
@RequiredArgsConstructor
public class FinishCommand implements Command {
    private final ScrapperWebService webService;

    private static final String COMMAND = "/finish";
    private static final String BYE_MESSAGE = "Bye!";

    @Override
    public SendMessage handle(Message message) {
        webService.deleteChat(message.getChatId());
        return new SendMessage(message.getChatId().toString(), BYE_MESSAGE);
    }

    @Override
    public boolean supports(Message message) {
        return message.getText().equals(COMMAND);
    }
}
