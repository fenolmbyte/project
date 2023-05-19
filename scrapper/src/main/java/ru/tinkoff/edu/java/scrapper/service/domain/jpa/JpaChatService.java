package ru.tinkoff.edu.java.scrapper.service.domain.jpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dto.entity.ChatEntity;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.domain.api.ChatService;

@Slf4j
@RequiredArgsConstructor
public class JpaChatService implements ChatService {
    private final JpaChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;

    @Override
    @Transactional
    public void register(Long id) {
        if (chatRepository.existsById(id)) {
            throw new IllegalArgumentException("Chat already registered");
        }
        chatRepository.saveAndFlush(new ChatEntity(id));
    }

    @Override
    @Transactional
    public void unregister(Long id) {
        if (!chatRepository.existsById(id)) {
            throw new IllegalArgumentException("Chat does not exist");
        }
        chatRepository.deleteById(id);
        linkRepository.deleteWithZeroSubscribers();
    }
}
