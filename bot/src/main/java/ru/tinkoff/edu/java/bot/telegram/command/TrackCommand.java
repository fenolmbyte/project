package ru.tinkoff.edu.java.bot.telegram.command;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tinkoff.edu.java.bot.exception.LinkIsAlreadyTackingException;
import ru.tinkoff.edu.java.bot.service.ScrapperWebService;
import ru.tinkoff.edu.java.parser.data.LinkData;
import ru.tinkoff.edu.java.parser.handler.LinkHandlerChain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Order(4)
@Slf4j
@Component
public class TrackCommand extends AbstractPublicCommand {
    private final ScrapperWebService webService;
    private final LinkHandlerChain linkHandler;

    private static final String COMMAND = "/track";
    private static final String DESCRIPTION = "start tracking link";
    private static final Pattern PATTERN = Pattern.compile("^\\s*/track (\\S+)\\s*$");
    private static final String SUCCESS_RESPONSE = "Added link to your tacking list";
    private static final String WRONG_FORMAT_RESPONSE = "Use correct format: '\\track <link>'";
    private static final String WRONG_LINK_FORMAT_RESPONSE =
            "You can only use correct GitHub links for repos and StackOverflow links for questions";
    private static final String LINK_IS_ALREADY_TRACKING_RESPONSE = "You are already tracking this link";

    public TrackCommand(ScrapperWebService webService, LinkHandlerChain linkHandler) {
        super(COMMAND, DESCRIPTION);
        this.webService = webService;
        this.linkHandler = linkHandler;
    }

    @Override
    public SendMessage handle(@NotNull Message message) {
        String text = message.getText();
        Matcher matcher = PATTERN.matcher(text);
        if (!matcher.matches()) {
            return new SendMessage(message.getChatId().toString(), WRONG_FORMAT_RESPONSE);
        }
        String url = matcher.group(1);
        LinkData linkData = linkHandler.handle(url);
        if (linkData == null) {
            return new SendMessage(message.getChatId().toString(), WRONG_LINK_FORMAT_RESPONSE);
        }
        try {
            webService.createLink(message.getChatId(), url);
        } catch (LinkIsAlreadyTackingException ignored) {
            return new SendMessage(message.getChatId().toString(), LINK_IS_ALREADY_TRACKING_RESPONSE);
        }
        log.info("Created link {}", url);
        return new SendMessage(message.getChatId().toString(), SUCCESS_RESPONSE);
    }

    @Override
    public boolean supports(@NotNull Message message) {
        return message.getText().trim().startsWith(COMMAND);
    }
}
