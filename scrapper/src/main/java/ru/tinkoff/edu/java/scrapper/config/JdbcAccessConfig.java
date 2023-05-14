package ru.tinkoff.edu.java.scrapper.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.service.domain.api.ChatService;
import ru.tinkoff.edu.java.scrapper.service.domain.api.LinkService;
import ru.tinkoff.edu.java.scrapper.service.domain.api.SubscriptionService;
import ru.tinkoff.edu.java.scrapper.service.domain.jdbc.JdbcChatService;
import ru.tinkoff.edu.java.scrapper.service.domain.jdbc.JdbcLinkService;
import ru.tinkoff.edu.java.scrapper.service.domain.jdbc.JdbcSubscriptionService;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfig {
    @Bean
    public JdbcChatRepository chatRepository(JdbcTemplate template) {
        return new JdbcChatRepository(template);
    }

    @Bean
    public JdbcLinkRepository linkRepository(JdbcTemplate template) {
        return new JdbcLinkRepository(template);
    }

    @Bean
    public JdbcSubscriptionRepository subscriptionRepository(JdbcTemplate template) {
        return new JdbcSubscriptionRepository(template);
    }

    @Bean
    public LinkService linkService(JdbcLinkRepository linkRepository) {
        return new JdbcLinkService(linkRepository);
    }

    @Bean
    public ChatService chatService(
            JdbcLinkRepository linkRepository,
            JdbcChatRepository chatRepository
    ) {
        return new JdbcChatService(chatRepository, linkRepository);
    }

    @Bean
    public SubscriptionService subscriptionService(
            JdbcLinkRepository linkRepository,
            JdbcChatRepository chatRepository,
            JdbcSubscriptionRepository subscriptionRepository
    ) {
        return new JdbcSubscriptionService(
                subscriptionRepository,
                linkRepository,
                chatRepository
        );
    }
}
