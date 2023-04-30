package ru.tinkoff.edu.java.bot.telegram.command;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Getter
public abstract class AbstractPublicCommand implements Command {
    private final String command;
    private final String description;

    public AbstractPublicCommand(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public BotCommand toBotCommand() {
        return new BotCommand(command, description);
    }
}
