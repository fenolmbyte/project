package ru.tinkoff.edu.java.bot.telegram.command;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Order(1)
public class HelpCommand extends AbstractPublicCommand {
    private static final String COMMAND = "/help";
    private static final String DESCRIPTION = "список доступных команд";

    private final List<String> commandsDescription;

    public HelpCommand(@Lazy List<AbstractPublicCommand> publicCommands) {
        super(COMMAND, DESCRIPTION);
        commandsDescription = publicCommands
                .stream()
                .map(c -> c.getCommand() + ": " + c.getDescription())
                .toList();
    }

    @Override
    public SendMessage handle(@NotNull Message message) {
        return new SendMessage(message.getChatId().toString(),
                "Описание команды: \n" + Strings.join(commandsDescription, '\n'));
    }

    @Override
    public boolean supports(@NotNull Message message) {
        return message.getText().trim().startsWith(COMMAND);
    }
}
