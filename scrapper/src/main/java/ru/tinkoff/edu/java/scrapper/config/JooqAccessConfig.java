package ru.tinkoff.edu.java.scrapper.config;

import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.service.domain.api.ChatService;
import ru.tinkoff.edu.java.scrapper.service.domain.api.LinkService;
import ru.tinkoff.edu.java.scrapper.service.domain.api.SubscriptionService;
import ru.tinkoff.edu.java.scrapper.service.domain.jooq.JooqChatService;
import ru.tinkoff.edu.java.scrapper.service.domain.jooq.JooqLinkService;
import ru.tinkoff.edu.java.scrapper.service.domain.jooq.JooqSubscriptionService;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfig {
    @Bean
    public JooqChatRepository chatRepository(DSLContext context) {
        return new JooqChatRepository(context);
    }

    @Bean
    public JooqLinkRepository linkRepository(DSLContext context) {
        return new JooqLinkRepository(context);
    }

    @Bean
    public JooqSubscriptionRepository subscriptionRepository(DSLContext context) {
        return new JooqSubscriptionRepository(context);
    }

    @Bean
    public LinkService linkService(
            JooqLinkRepository linkRepository
    ) {
        return new JooqLinkService(linkRepository);
    }

    @Bean
    public ChatService chatService(
            JooqLinkRepository linkRepository,
            JooqChatRepository chatRepository
    ) {
        return new JooqChatService(linkRepository, chatRepository);
    }

    @Bean
    public SubscriptionService subscriptionService(
            JooqLinkRepository linkRepository,
            JooqChatRepository chatRepository,
            JooqSubscriptionRepository subscriptionRepository
    ) {
        return new JooqSubscriptionService(
                linkRepository,
                chatRepository,
                subscriptionRepository
        );
    }
}
