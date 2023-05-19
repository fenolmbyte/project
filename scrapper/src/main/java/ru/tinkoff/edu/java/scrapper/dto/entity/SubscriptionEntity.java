package ru.tinkoff.edu.java.scrapper.dto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
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
