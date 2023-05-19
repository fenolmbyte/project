package ru.tinkoff.edu.java.scrapper.repository.jooq;

import jakarta.annotation.Nullable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Subscription;
import ru.tinkoff.edu.java.scrapper.dto.model.Chat;

@Repository
@RequiredArgsConstructor
public class JooqChatRepository {
    private final DSLContext context;
    private final ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Chat chat =
        ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Chat.CHAT;
    private final Subscription subscription = Subscription.SUBSCRIPTION;

    public void add(Long id) {
        context.insertInto(chat)
            .set(chat.ID, id)
            .execute();
    }

    public void removeById(Long id) {
        context.delete(chat)
            .where(chat.ID.eq(id))
            .execute();
    }

    public @Nullable Chat findById(Long id) {
        return context.select(chat.fields())
            .from(chat)
            .fetchOneInto(Chat.class);
    }

    public List<Chat> findAll() {
        return context.select(chat.fields())
            .from(chat)
            .fetchInto(Chat.class);
    }

    public List<Chat> findAllSubscribers(Long linkId) {
        return context.select(chat.fields())
            .from(chat)
            .join(subscription).on(chat.ID.eq(subscription.CHAT_ID))
            .where(subscription.LINK_ID.eq(linkId))
            .fetchInto(Chat.class);
    }
}
