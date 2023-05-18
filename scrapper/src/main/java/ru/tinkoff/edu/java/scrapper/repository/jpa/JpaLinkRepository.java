package ru.tinkoff.edu.java.scrapper.repository.jpa;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.dto.entity.LinkEntity;

@Repository
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public interface JpaLinkRepository extends JpaRepository<LinkEntity, Long> {
    @Query("""
        delete from LinkEntity l
        where l.id in
            (select l.id from LinkEntity l
            left outer join SubscriptionEntity s on l.id = s.linkId
            where s.linkId is NULL)
        """)
    @Modifying
    void deleteWithZeroSubscribers();

    @Query(value = """
        update link
        set last_check_time = now()
        where :timeParam > last_check_time
        returning id, url, last_check_time, last_update_time
        """, nativeQuery = true)
    @Modifying(clearAutomatically = true)
    List<LinkEntity> updateLastCheckedTimeAndGet(@Param("timeParam") OffsetDateTime shouldBeCheckedAfter);

    Optional<LinkEntity> findLinkEntityByUrl(String url);

    @Query("select l from LinkEntity l left join fetch l.subscribers where l.id = :id")
    Optional<LinkEntity> findById(@Param("id") @NotNull Long id);
}
