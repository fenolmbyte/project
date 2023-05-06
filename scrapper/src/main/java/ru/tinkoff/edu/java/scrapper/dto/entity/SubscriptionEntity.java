package ru.tinkoff.edu.java.scrapper.dto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "subscription")
@IdClass(SubscriptionPk.class)
@NoArgsConstructor
@Getter
@Setter
public class SubscriptionEntity {
    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Id
    @Column(name = "link_id")
    private Long linkId;

    public SubscriptionEntity(Long chatId, Long linkId) {
        this.chatId = chatId;
        this.linkId = linkId;
    }
}
