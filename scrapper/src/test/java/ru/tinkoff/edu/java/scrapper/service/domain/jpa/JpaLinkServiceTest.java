package ru.tinkoff.edu.java.scrapper.service.domain.jpa;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.dto.model.Link;
import ru.tinkoff.edu.java.scrapper.exception.InternalError;
import ru.tinkoff.edu.java.scrapper.service.domain.api.LinkService;

@SpringBootTest
class JpaLinkServiceTest extends IntegrationEnvironment {
    @Autowired
    private LinkService linkService;

    @Autowired
    private ServicesTestHelper helper;

    @Test
    @Transactional
    @Rollback
    void updateLastCheckedTimeAndGet__someLinksUnchecked_updatesUncheckedLinks() {
        // given
        OffsetDateTime lastUpdateTime = OffsetDateTime.now();

        Long id1 = 1L;
        String url1 = "https://github.com/fenolmbyte/project-1/";
        OffsetDateTime lastCheckTime1 = OffsetDateTime.now().minusMinutes(5);
        helper.addLink(id1, url1, lastCheckTime1, lastUpdateTime);

        Long id2 = 2L;
        String url2 = "https://github.com/fenolmbyte/project-2/";
        OffsetDateTime lastCheckTime2 = OffsetDateTime.now().minusMinutes(5);
        helper.addLink(id2, url2, lastCheckTime2, lastUpdateTime);

        Long id3 = 3L;
        String url3 = "https://github.com/fenolmbyte/project-3/";
        OffsetDateTime lastCheckTime3 = OffsetDateTime.now();
        helper.addLink(id3, url3, lastCheckTime3, lastUpdateTime);

        Duration durationTenSeconds = Duration.ofSeconds(10);

        // when
        List<Link> links = linkService.updateLastCheckedTimeAndGet(durationTenSeconds);

        // then
        assertEquals(links.size(), 2);
        assertEquals(links.get(0).getId(), id1);
        assertEquals(links.get(1).getId(), id2);
        assertEquals(helper.getLinkById(id3).getLastCheckTime().toEpochSecond(), lastCheckTime3.toEpochSecond());
    }

    @Test
    @Transactional
    @Rollback
    void updateLink__linkExists_updatesLink() {
        // given
        Long id = 1L;
        String url = "https://github.com/fenolmbyte/project/";
        helper.addLink(id, url);
        OffsetDateTime newUpdateTime = OffsetDateTime.now().plusHours(5);

        // when

        // then
        linkService.updateLinkLastUpdateTime(id, newUpdateTime);
        Link updatedLink = helper.getLinkById(id);
        assertEquals(newUpdateTime.toEpochSecond(), updatedLink.getLastUpdateTime().toEpochSecond());
    }

    @Test
    @Transactional
    @Rollback
    void updateLink__linkDoesNotExist_throwsException() {
        // given
        Long id = 1L;
        assertNull(helper.getLinkById(id));
        OffsetDateTime newUpdateTime = OffsetDateTime.now();

        // when

        // then
        assertThrows(InternalError.class, () -> linkService.updateLinkLastUpdateTime(id, newUpdateTime));
    }
}
