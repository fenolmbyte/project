package ru.tinkoff.edu.java.scrapper.repository.jooq;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Chat;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Subscription;
import ru.tinkoff.edu.java.scrapper.dto.entity.ChatEntity;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JooqChatRepository {
    private final DSLContext context;
    private final Chat chat = Chat.CHAT;
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

    public @Nullable ChatEntity findById(Long id) {
        return context.select(chat.fields())
                .from(chat)
                .fetchOneInto(ChatEntity.class);
    }

    public List<ChatEntity> findAll() {
        return context.select(chat.fields())
                .from(chat)
                .fetchInto(ChatEntity.class);
    }

    public List<ChatEntity> findAllSubscribers(Long linkId) {
        return context.select(chat.fields())
                .from(chat)
                .join(subscription).on(chat.ID.eq(subscription.CHAT_ID))
                .where(subscription.LINK_ID.eq(linkId))
                .fetchInto(ChatEntity.class);
    }
}
