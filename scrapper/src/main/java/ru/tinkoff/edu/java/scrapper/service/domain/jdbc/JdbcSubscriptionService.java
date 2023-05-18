package ru.tinkoff.edu.java.scrapper.service.domain.jdbc;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dto.model.Chat;
import ru.tinkoff.edu.java.scrapper.dto.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.service.domain.api.SubscriptionService;

@Slf4j
@RequiredArgsConstructor
public class JdbcSubscriptionService implements SubscriptionService {
    private final JdbcSubscriptionRepository subscriptionRepository;
    private final JdbcLinkRepository linkRepository;
    private final JdbcChatRepository chatRepository;

    @Override
    @Transactional
    public Link subscribe(Long chatId, URI url) {
        try {
            Link link = linkRepository.find(url.toString());
            subscriptionRepository.add(chatId, link.getId());
            return link;
        } catch (EmptyResultDataAccessException ignored) {
            Long linkId = linkRepository.add(url.toString());
            try {
                subscriptionRepository.add(chatId, linkId);
            } catch (DuplicateKeyException e) {
                log.error(e.getMessage());
                throw new IllegalArgumentException("Subscription already exist", e);
            }
            return linkRepository.findById(linkId);
        }
    }

    @Override
    @Transactional
    public Link unsubscribe(Long chatId, URI url) {
        try {
            Link link = linkRepository.find(url.toString());
            subscriptionRepository.remove(chatId, link.getId());
            Integer subscriptionsCount = subscriptionRepository.countSubscriptions(link.getId());
            if (subscriptionsCount == 0) {
                linkRepository.removeById(link.getId());
            }
            return link;
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("Link not found", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Link> getChatSubscriptions(Long chatId) {
        return linkRepository.findWithSubscriber(chatId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Chat> getLinkSubscribers(Long linkId) {
        return chatRepository.findAllSubscribers(linkId);
    }
}
