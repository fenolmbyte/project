package ru.tinkoff.edu.java.scrapper.service.domain.api;

import ru.tinkoff.edu.java.scrapper.dto.model.Link;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {
    List<Link> updateLastCheckedTimeAndGet(Duration linkToBeCheckedInterval);
    void updateLinkLastUpdateTime(Long id, OffsetDateTime newUpdateTime);
}
