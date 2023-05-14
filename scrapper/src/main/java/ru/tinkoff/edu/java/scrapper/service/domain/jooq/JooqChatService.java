package ru.tinkoff.edu.java.scrapper.service.domain.jooq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.domain.api.ChatService;

@Slf4j
@RequiredArgsConstructor
public class JooqChatService implements ChatService {
    private final JooqLinkRepository linkRepository;
    private final JooqChatRepository chatRepository;

    @Override
    @Transactional
    public void register(Long id) {
        if (chatRepository.findById(id) == null) {
            chatRepository.add(id);
        } else {
            log.error("Chat duplication error");
            throw new IllegalArgumentException("Chat already registered");
        }
    }

    @Override
    @Transactional
    public void unregister(Long id) {
        chatRepository.removeById(id);
        linkRepository.removeWithZeroSubscriptions();
    }
}
