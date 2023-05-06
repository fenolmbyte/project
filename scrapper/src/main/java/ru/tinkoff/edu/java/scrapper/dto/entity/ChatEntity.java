package ru.tinkoff.edu.java.scrapper.dto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "chat")
@Getter
@Setter
@NoArgsConstructor
public class ChatEntity {
    @Id
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "subscription",
            joinColumns = @JoinColumn(name = "chat_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "link_id", referencedColumnName = "id")
    )
    private List<LinkEntity> subscriptions;

    public ChatEntity(Long id) {
        this.id = id;
    }
}
