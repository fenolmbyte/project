package ru.tinkoff.edu.java.scrapper.service.domain.api;

import ru.tinkoff.edu.java.scrapper.dto.entity.LinkEntity;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {
    List<LinkEntity> updateLastCheckedTimeAndGet(Duration linkToBeCheckedInterval);
    void updateLink(LinkEntity linkEntity, OffsetDateTime newUpdateTime);
}
