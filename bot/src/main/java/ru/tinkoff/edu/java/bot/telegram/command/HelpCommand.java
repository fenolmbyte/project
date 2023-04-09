package ru.tinkoff.edu.java.bot.telegram.command;

import jakarta.validation.constraints.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Order(1)
public class HelpCommand extends AbstractPublicCommand {
    private static final String COMMAND = "/help";
    private static final String DESCRIPTION = "shows a list of commands";

    public HelpCommand() {
        super(COMMAND, DESCRIPTION);
    }

    @Override
    public SendMessage handle(@NotNull Message message) {
        // TODO: implement
        return new SendMessage(message.getChatId().toString(), "*your help is here*");
    }

    @Override
    public boolean supports(@NotNull Message message) {
        return message.getText().trim().startsWith(COMMAND);
    }
}
