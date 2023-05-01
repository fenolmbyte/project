package ru.tinkoff.edu.java.scrapper.repository.jooq;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Link;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Subscription;
import ru.tinkoff.edu.java.scrapper.dto.entity.LinkEntity;

import java.time.OffsetDateTime;
import java.util.List;

import static org.jooq.impl.DSL.currentOffsetDateTime;
import static org.jooq.impl.DSL.select;

@Repository
@RequiredArgsConstructor
public class JooqLinkRepository {
    private final DSLContext context;
    private final Link link = Link.LINK;
    private final Subscription subscription = Subscription.SUBSCRIPTION;

    public LinkEntity subscribe(String url, Long chatId) {
        LinkEntity linkEntity = find(url);
        if (linkEntity == null) {
            linkEntity = context.insertInto(link)
                    .set(link.URL, url)
                    .returning(link.fields())
                    .fetchOneInto(LinkEntity.class);
        }
        context.insertInto(subscription)
                .set(subscription.LINK_ID, linkEntity.getId())
                .set(subscription.CHAT_ID, chatId)
                .execute();
        return linkEntity;
    }

    public @Nullable LinkEntity find(String url) {
        return context.select(link.fields())
                .from(link)
                .where(link.URL.eq(url))
                .fetchOneInto(LinkEntity.class);
    }

    public @Nullable LinkEntity findById(Long id) {
        return context.select(link.fields())
                .from(link)
                .where(link.ID.eq(id))
                .fetchOneInto(LinkEntity.class);
    }

    public List<LinkEntity> findAll() {
        return context.select(link.fields())
                .from(link)
                .fetchInto(LinkEntity.class);
    }

    public List<LinkEntity> findWithChatSubscription(Long chatId) {
        return context.select(link.fields())
                .from(link)
                .join(subscription).on(link.ID.eq(subscription.LINK_ID))
                .where(subscription.CHAT_ID.eq(chatId))
                .fetchInto(LinkEntity.class);
    }

    public List<LinkEntity> findWithLastCheckedTimeLongAgo(OffsetDateTime shouldBeCheckedAfter) {
        return context.update(link)
                .set(link.LAST_CHECK_TIME, currentOffsetDateTime())
                .where(link.LAST_CHECK_TIME.lessThan(shouldBeCheckedAfter))
                .returning(link.fields())
                .fetchInto(LinkEntity.class);
    }

    public void updateLastUpdateTime(Long id, OffsetDateTime newUpdateTime) {
        context.update(link)
                .set(link.LAST_UPDATE_TIME, newUpdateTime)
                .where(link.ID.eq(id))
                .execute();
    }

    public void remove(String url) {
        context.delete(link)
                .where(link.URL.eq(url))
                .execute();
    }

    public void removeById(Long id) {
        context.delete(link)
                .where(link.ID.eq(id))
                .execute();
    }

    public void removeWithZeroSubscriptions() {
        context.delete(link)
                .where(link.ID.in(
                        select(link.ID).from(link)
                                .leftOuterJoin(subscription)
                                .on(subscription.LINK_ID.eq(link.ID))
                                .where(subscription.CHAT_ID.isNull())
                ));
    }
}
