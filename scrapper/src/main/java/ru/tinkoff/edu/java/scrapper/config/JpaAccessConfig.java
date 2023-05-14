package ru.tinkoff.edu.java.scrapper.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.service.domain.api.ChatService;
import ru.tinkoff.edu.java.scrapper.service.domain.api.LinkService;
import ru.tinkoff.edu.java.scrapper.service.domain.api.SubscriptionService;
import ru.tinkoff.edu.java.scrapper.service.domain.jpa.JpaChatService;
import ru.tinkoff.edu.java.scrapper.service.domain.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.service.domain.jpa.JpaSubscriptionService;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfig {
    @Bean
    public LinkService linkService(JpaLinkRepository linkRepository) {
        return new JpaLinkService(linkRepository);
    }

    @Bean
    public ChatService chatService(
            JpaLinkRepository linkRepository,
            JpaChatRepository chatRepository
    ) {
        return new JpaChatService(chatRepository, linkRepository);
    }

    @Bean
    public SubscriptionService subscriptionService(
            JpaLinkRepository linkRepository,
            JpaChatRepository chatRepository,
            JpaSubscriptionRepository subscriptionRepository
    ) {
        return new JpaSubscriptionService(
                subscriptionRepository,
                linkRepository,
                chatRepository
        );
    }
}
