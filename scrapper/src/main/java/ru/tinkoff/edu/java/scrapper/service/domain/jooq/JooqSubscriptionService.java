package ru.tinkoff.edu.java.scrapper.service.domain.jooq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dto.entity.ChatEntity;
import ru.tinkoff.edu.java.scrapper.dto.entity.LinkEntity;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.service.domain.api.SubscriptionService;

import java.net.URI;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JooqSubscriptionService implements SubscriptionService {
    private final JooqLinkRepository linkRepository;
    private final JooqChatRepository chatRepository;
    private final JooqSubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public LinkEntity subscribe(Long chatId, URI url) {
        try {
            return linkRepository.subscribe(url.toString(), chatId);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("Subscription already exist", e);
        }
    }

    @Override
    @Transactional
    public LinkEntity unsubscribe(Long chatId, URI url) {
        LinkEntity linkEntity = linkRepository.find(url.toString());
        if (linkEntity != null) {
            subscriptionRepository.remove(chatId, linkEntity.getId());
            Integer subscriptions = subscriptionRepository.countSubscriptions(linkEntity.getId());
            if (subscriptions == 0) {
                linkRepository.removeById(linkEntity.getId());
            }
            return linkEntity;
        } else {
            log.error("Link not found");
            throw new IllegalArgumentException("Link not found");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LinkEntity> getChatSubscriptions(Long chatId) {
        return linkRepository.findWithChatSubscription(chatId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatEntity> getLinkSubscribers(Long linkId) {
        return chatRepository.findAllSubscribers(linkId);
    }
}
