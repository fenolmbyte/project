package ru.tinkoff.edu.java.scrapper.service;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.parser.data.GitHubLinkData;
import ru.tinkoff.edu.java.parser.data.LinkData;
import ru.tinkoff.edu.java.parser.data.StackOverflowLinkData;
import ru.tinkoff.edu.java.parser.handler.LinkHandlerChain;
import ru.tinkoff.edu.java.scrapper.config.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.dto.bot.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.client.UpdatesInfo;
import ru.tinkoff.edu.java.scrapper.dto.entity.LinkEntity;
import ru.tinkoff.edu.java.scrapper.exception.InternalError;
import ru.tinkoff.edu.java.scrapper.service.api.LinkService;
import ru.tinkoff.edu.java.scrapper.service.api.SubscriptionService;
import ru.tinkoff.edu.java.scrapper.service.client.BotWebService;
import ru.tinkoff.edu.java.scrapper.service.client.GitHubWebService;
import ru.tinkoff.edu.java.scrapper.service.client.StackOverflowWebService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkUpdatesService {
    private final ApplicationConfig config;
    private final LinkHandlerChain handlerChain;
    private final LinkService linkService;
    private final SubscriptionService subscriptionService;
    private final GitHubWebService gitHubWebService;
    private final StackOverflowWebService stackOverflowWebService;
    private final BotWebService botWebService;

    private List<LinkEntity> getUncheckedLinks() {
        return linkService.updateLastCheckedTimeAndGet(
                config.getScheduler().getLinkToBeCheckedInterval()
        );
    }

    private @Nullable UpdatesInfo fetchUpdates(LinkEntity link) {
        LinkData linkData = handlerChain.handle(link.getUrl());
        return switch (linkData) {
            case null -> throw new InternalError("Malicious link");
            case GitHubLinkData data ->
                    gitHubWebService.fetchEventsUpdates(data.owner(), data.repo(), link.getLastUpdateTime());
            case StackOverflowLinkData data -> stackOverflowWebService.fetchQuestionUpdates(data.questionId());
        };
    }

    private void sendUpdates(LinkEntity link, UpdatesInfo updatesInfo) {
        linkService.updateLink(link, updatesInfo.lastUpdateTime());
        botWebService.sendUpdate(new LinkUpdateRequest(
                link.getId(),
                link.getUrl(),
                Strings.join(updatesInfo.updates(), '\n'),
                subscriptionService.getChatsIds(link.getId())
        ));
    }

    public void updateLinks() {
        getUncheckedLinks().forEach(link -> {
            UpdatesInfo updatesInfo = fetchUpdates(link);

            boolean shouldSendUpdate = updatesInfo != null &&
                    (link.getLastUpdateTime() == null ||
                            link.getLastUpdateTime().isBefore(updatesInfo.lastUpdateTime()));
            if (shouldSendUpdate) {
                sendUpdates(link, updatesInfo);
            }
        });
    }
}
