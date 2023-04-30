package ru.tinkoff.edu.java.scrapper.dto.client;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public record UpdatesInfo(
        OffsetDateTime lastUpdateTime,
        List<String> updates
) {
    public UpdatesInfo(OffsetDateTime lastUpdateTime) {
        this(lastUpdateTime, new ArrayList<>());
    }
}
