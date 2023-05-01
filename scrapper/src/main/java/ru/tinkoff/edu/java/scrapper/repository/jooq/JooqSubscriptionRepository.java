package ru.tinkoff.edu.java.scrapper.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Subscription;
import ru.tinkoff.edu.java.scrapper.dto.entity.SubscriptionEntity;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JooqSubscriptionRepository {
    private final DSLContext context;
    private final Subscription subscription = Subscription.SUBSCRIPTION;

    public void add(Long chatId, Long linkId) {
        context.insertInto(subscription)
                .set(subscription.CHAT_ID, chatId)
                .set(subscription.LINK_ID, linkId)
                .execute();
    }

    public List<SubscriptionEntity> findAll() {
        return context.select(subscription.fields())
                .from(subscription)
                .fetchInto(SubscriptionEntity.class);
    }

    public void remove(Long chatId, Long linkId) {
        context.delete(subscription)
                .where(subscription.LINK_ID.eq(linkId)
                        .and(subscription.CHAT_ID.eq(chatId)))
                .execute();
    }

    public Integer countSubscriptions(Long linkId) {
        return context.fetchCount(subscription, subscription.LINK_ID.eq(linkId));
    }
}
