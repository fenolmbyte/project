package ru.tinkoff.edu.java.scrapper.service.domain.jpa;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dto.entity.ChatEntity;
import ru.tinkoff.edu.java.scrapper.dto.entity.LinkEntity;
import ru.tinkoff.edu.java.scrapper.dto.entity.SubscriptionEntity;
import ru.tinkoff.edu.java.scrapper.dto.entity.SubscriptionPk;
import ru.tinkoff.edu.java.scrapper.dto.model.Chat;
import ru.tinkoff.edu.java.scrapper.dto.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.service.domain.api.SubscriptionService;

@Slf4j
@RequiredArgsConstructor
public class JpaSubscriptionService implements SubscriptionService {
    private final JpaSubscriptionRepository subscriptionRepository;
    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;

    private final static String CHAT_DOES_NOT_EXIST_MESSAGE = "Chat does not exist";
    private final static String LINK_DOES_NOT_EXIST_MESSAGE = "Link does not exist";
    private final static String SUBSCRIPTION_ALREADY_EXIST_MESSAGE = "Subscription already exist";

    @Override
    @Transactional
    public Link subscribe(Long chatId, URI url) {
        if (!chatRepository.existsById(chatId)) {
            throw new IllegalArgumentException(CHAT_DOES_NOT_EXIST_MESSAGE);
        }
        Optional<LinkEntity> linkEntityOptional = linkRepository.findLinkEntityByUrl(url.toString());
        LinkEntity linkEntity = linkEntityOptional.orElseGet(
            () -> linkRepository.saveAndFlush(new LinkEntity(url.toString()))
        );
        if (subscriptionRepository.existsById(new SubscriptionPk(chatId, linkEntity.getId()))) {
            throw new IllegalArgumentException(SUBSCRIPTION_ALREADY_EXIST_MESSAGE);
        }
        subscriptionRepository.saveAndFlush(new SubscriptionEntity(chatId, linkEntity.getId()));
        return new Link(linkEntity);
    }

    @Override
    @Transactional
    public Link unsubscribe(Long chatId, URI url) {
        if (!chatRepository.existsById(chatId)) {
            throw new IllegalArgumentException(CHAT_DOES_NOT_EXIST_MESSAGE);
        }
        Optional<LinkEntity> linkEntityOptional = linkRepository.findLinkEntityByUrl(url.toString());
        if (linkEntityOptional.isEmpty()) {
            throw new IllegalArgumentException(LINK_DOES_NOT_EXIST_MESSAGE);
        }
        LinkEntity linkEntity = linkEntityOptional.get();
        subscriptionRepository.deleteById(new SubscriptionPk(chatId, linkEntity.getId()));
        Integer subscriptionsCount = subscriptionRepository.countByLinkId(linkEntity.getId());
        if (subscriptionsCount == 0) {
            linkRepository.deleteById(linkEntity.getId());
        }
        linkRepository.flush();
        subscriptionRepository.flush();
        return new Link(linkEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Link> getChatSubscriptions(Long chatId) {
        Optional<ChatEntity> chatEntityOptional = chatRepository.findById(chatId);
        if (chatEntityOptional.isEmpty()) {
            throw new IllegalArgumentException(CHAT_DOES_NOT_EXIST_MESSAGE);
        }
        ChatEntity chatEntity = chatEntityOptional.get();
        return chatEntity.getSubscriptions().stream()
            .map(Link::new)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Chat> getLinkSubscribers(Long linkId) {
        Optional<LinkEntity> linkEntityOptional = linkRepository.findById(linkId);
        if (linkEntityOptional.isEmpty()) {
            throw new IllegalArgumentException(LINK_DOES_NOT_EXIST_MESSAGE);
        }
        LinkEntity linkEntity = linkEntityOptional.get();
        return linkEntity.getSubscribers().stream()
            .map(Chat::new)
            .toList();
    }
}
