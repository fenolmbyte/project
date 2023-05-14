package ru.tinkoff.edu.java.scrapper.dto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "link")
@Getter
@Setter
@NoArgsConstructor
public class LinkEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "linkIdSeq"
    )
    @SequenceGenerator(
            name = "linkIdSeq",
            sequenceName = "link_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "url", nullable = false, unique = true)
    private String url;

    @Column(name = "last_check_time", nullable = false)
    private OffsetDateTime lastCheckTime = OffsetDateTime.now();

    @Column(name = "last_update_time", nullable = false)
    private OffsetDateTime lastUpdateTime = OffsetDateTime.now();

    @ManyToMany
    @JoinTable(
            name = "subscription",
            joinColumns = @JoinColumn(name = "link_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id", referencedColumnName = "id")
    )
    private List<ChatEntity> subscribers;

    public LinkEntity(String url) {
        this.url = url;
    }
}
