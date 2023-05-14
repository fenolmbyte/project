package ru.tinkoff.edu.java.scrapper.service.domain.jpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dto.entity.LinkEntity;
import ru.tinkoff.edu.java.scrapper.dto.model.Link;
import ru.tinkoff.edu.java.scrapper.exception.InternalError;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.domain.api.LinkService;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaLinkRepository linkRepository;

    @Override
    @Transactional
    public List<Link> updateLastCheckedTimeAndGet(Duration linkToBeCheckedInterval) {
        log.info(OffsetDateTime.now().minusNanos(linkToBeCheckedInterval.toNanos()).toString());
        return linkRepository.updateLastCheckedTimeAndGet(
                        OffsetDateTime.now().minusNanos(linkToBeCheckedInterval.toNanos())
                ).stream()
                .map(Link::new)
                .toList();
    }

    @Override
    @Transactional
    public void updateLinkLastUpdateTime(Long id, OffsetDateTime newUpdateTime) {
        Optional<LinkEntity> linkEntityOptional = linkRepository.findById(id);
        if (linkEntityOptional.isEmpty()) {
            throw new InternalError("Link does not exist");
        }
        LinkEntity linkEntity = linkEntityOptional.get();
        linkEntity.setLastUpdateTime(newUpdateTime);
        linkRepository.saveAndFlush(linkEntity);
    }
}
