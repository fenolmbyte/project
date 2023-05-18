package ru.tinkoff.edu.java.scrapper.service.domain.api;

import java.net.URI;
import java.util.List;
import ru.tinkoff.edu.java.scrapper.dto.model.Chat;
import ru.tinkoff.edu.java.scrapper.dto.model.Link;

public interface SubscriptionService {
    Link subscribe(Long chatId, URI url);

    Link unsubscribe(Long chatId, URI url);

    List<Link> getChatSubscriptions(Long chatId);

    List<Chat> getLinkSubscribers(Long linkId);

    default List<Long> getChatsIds(Long linkId) {
        return getLinkSubscribers(linkId)
            .stream()
            .map(Chat::getId)
            .toList();
    }
}
