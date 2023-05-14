package ru.tinkoff.edu.java.scrapper.service.domain.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dto.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.domain.api.LinkService;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository linkRepository;

    @Override
    @Transactional
    public List<Link> updateLastCheckedTimeAndGet(Duration linkToBeCheckedInterval) {
        return linkRepository.updateLastCheckedTimeAndGet(
                OffsetDateTime.now().minusNanos(linkToBeCheckedInterval.toNanos())
        );
    }

    @Override
    @Transactional
    public void updateLinkLastUpdateTime(Long id, OffsetDateTime newUpdateTime) {
        linkRepository.updateLastUpdateTime(id, newUpdateTime);
    }
}
