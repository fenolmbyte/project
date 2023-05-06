package ru.tinkoff.edu.java.scrapper.service.domain.jooq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.exception.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dto.model.Chat;
import ru.tinkoff.edu.java.scrapper.dto.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.service.domain.api.SubscriptionService;

import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JooqSubscriptionService implements SubscriptionService {
    private final JooqLinkRepository linkRepository;
    private final JooqChatRepository chatRepository;
    private final JooqSubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public Link subscribe(Long chatId, URI url) {
        try {
            return linkRepository.subscribe(url.toString(), chatId);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("Subscription already exist", e);
        }
    }

    @Override
    @Transactional
    public Link unsubscribe(Long chatId, URI url) {
        Link link = linkRepository.find(url.toString());
        if (link != null) {
            subscriptionRepository.remove(chatId, link.getId());
            Integer subscriptions = subscriptionRepository.countSubscriptions(link.getId());
            if (subscriptions == 0) {
                linkRepository.removeById(link.getId());
            }
            return link;
        } else {
            log.error("Link not found");
            throw new IllegalArgumentException("Link not found");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Link> getChatSubscriptions(Long chatId) {
        return linkRepository.findWithChatSubscription(chatId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Chat> getLinkSubscribers(Long linkId) {
        return chatRepository.findAllSubscribers(linkId);
    }
}
