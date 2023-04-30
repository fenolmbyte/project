package ru.tinkoff.edu.java.scrapper.dto.entity;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkEntity {
    private Long id;
    private String url;
    private OffsetDateTime lastCheckTime;
    @Nullable
    private OffsetDateTime lastUpdateTime;
}
