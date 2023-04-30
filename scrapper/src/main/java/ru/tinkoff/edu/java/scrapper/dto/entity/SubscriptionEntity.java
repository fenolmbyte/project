package ru.tinkoff.edu.java.scrapper.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionEntity {
    private Long chatId;
    private Long linkId;
}
