package ru.tinkoff.edu.java.scrapper.dto.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPk implements Serializable {
    private Long chatId;
    private Long linkId;
}
