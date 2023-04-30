package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dto.entity.LinkEntity;
import ru.tinkoff.edu.java.scrapper.repository.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.api.LinkService;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository linkRepository;

    @Override
    @Transactional
    public List<LinkEntity> updateLastCheckedTimeAndGet(Duration linkToBeCheckedInterval) {
        return linkRepository.updateLastCheckedTimeAndGet(
                OffsetDateTime.now().plusNanos(linkToBeCheckedInterval.toNanos())
        );
    }

    @Override
    @Transactional
    public void updateLink(LinkEntity linkEntity, OffsetDateTime newUpdateTime) {
        linkRepository.updateLastUpdateTime(linkEntity.getId(), newUpdateTime);
    }
}
