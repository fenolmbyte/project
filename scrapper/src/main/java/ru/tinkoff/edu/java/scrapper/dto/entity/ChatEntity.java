package ru.tinkoff.edu.java.scrapper.dto.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
