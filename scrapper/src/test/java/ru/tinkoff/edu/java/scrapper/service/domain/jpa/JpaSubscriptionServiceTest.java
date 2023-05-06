package ru.tinkoff.edu.java.scrapper.service.domain.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.dto.model.Chat;
import ru.tinkoff.edu.java.scrapper.dto.model.Link;
import ru.tinkoff.edu.java.scrapper.service.domain.api.SubscriptionService;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class JpaSubscriptionServiceTest extends IntegrationEnvironment {
    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private ServicesTestHelper helper;

    @Test
    @Transactional
    @Rollback
    void subscribe__linkExists_createsSubscription() {
        // given
        Long chatId = 1L;
        Long linkId = 1L;
        String url = "https://github.com/fenolmbyte/project/";
        helper.addChat(chatId);
        helper.addLink(linkId, url);

        // when
        subscriptionService.subscribe(chatId, URI.create(url));

        // then
        assertNotNull(helper.getSubscriptionById(chatId, linkId));
    }

    @Test
    @Transactional
    @Rollback
    void subscribe__linkDoesNotExist_createsLinkAndSubscription() {
        // given
        Long chatId = 5L;
        String url = "https://github.com/fenolmbyte/project/";
        helper.addChat(chatId);

        // when
        Link link = subscriptionService.subscribe(chatId, URI.create(url));

        // then
        assertNotNull(helper.getChatById(chatId), "Chat not null");
        assertNotNull(helper.getLinkById(link.getId()), "Link not null"); // Transaction error?
        assertNotNull(helper.getSubscriptionById(chatId, link.getId()), "Subscription not null"); // Transaction error?
    }

    @Test
    @Transactional
    @Rollback
    void subscribe__chatDoesNotExist_throwsException() {
        // given
        Long chatId = 1L;
        Long linkId = 1L;
        String url = "https://github.com/fenolmbyte/project/";
        assertNull(helper.getLinkById(linkId));
        assertNull(helper.getChatById(chatId));

        // when

        // then
        assertThrows(IllegalArgumentException.class,
                () -> subscriptionService.subscribe(chatId, URI.create(url)));
    }

    @Test
    @Transactional
    @Rollback
    void subscribe__alreadySubscribed_throwsException() {
        // given
        Long chatId = 1L;
        Long linkId = 1L;
        String url = "https://github.com/fenolmbyte/project/";
        helper.addLink(linkId, url);
        helper.addChat(chatId);
        helper.addSubscription(chatId, linkId);

        // when

        // then
        assertThrows(IllegalArgumentException.class,
                () -> subscriptionService.subscribe(chatId, URI.create(url)));
    }

    @Test
    @Transactional
    @Rollback
    void unsubscribe__chatDoesNotExist_throwsException() {
        // given
        Long chatId = 1L;
        Long linkId = 1L;
        String url = "https://github.com/fenolmbyte/project/";
        assertNull(helper.getChatById(chatId));
        helper.addLink(linkId, url);

        // when

        // then
        assertThrows(IllegalArgumentException.class,
                () -> subscriptionService.unsubscribe(chatId, URI.create(url)));
    }

    @Test
    @Transactional
    @Rollback
    void unsubscribe__linkDoesNotExist_throwsException() {
        // given
        Long chatId = 1L;
        Long linkId = 1L;
        String url = "https://github.com/fenolmbyte/project/";
        helper.addChat(chatId);
        assertNull(helper.getLinkById(linkId));

        // when

        // then
        assertThrows(IllegalArgumentException.class,
                () -> subscriptionService.unsubscribe(chatId, URI.create(url)));
    }

    @Test
    @Transactional
    @Rollback
    void unsubscribe__linkHasOnlySubscriber_removesLinkAndSubscription() {
        // given
        Long chatId = 1L;
        Long linkId = 1L;
        String url = "https://github.com/fenolmbyte/project/";
        helper.addChat(chatId);
        helper.addLink(linkId, url);
        helper.addSubscription(chatId, linkId);

        // when
        assertNotNull(helper.getLinkById(linkId));
        assertNotNull(helper.getSubscriptionById(chatId, linkId));
        subscriptionService.unsubscribe(chatId, URI.create(url));

        // then
        assertNull(helper.getSubscriptionById(chatId, linkId));
        assertNull(helper.getLinkById(linkId));
    }

    @Test
    @Transactional
    @Rollback
    void unsubscribe__linkHasTwoSubscribers_removesOnlySubscription() {
        // given
        Long chatId1 = 1L;
        Long chatId2 = 2L;
        Long linkId = 1L;
        String url = "https://github.com/fenolmbyte/project/";
        helper.addChat(chatId1);
        helper.addChat(chatId2);
        helper.addLink(linkId, url);
        helper.addSubscription(chatId1, linkId);
        helper.addSubscription(chatId2, linkId);

        // when
        assertNotNull(helper.getLinkById(linkId));
        subscriptionService.unsubscribe(chatId1, URI.create(url));

        // then
        assertNotNull(helper.getLinkById(linkId));
        assertNull(helper.getSubscriptionById(chatId1, linkId));
    }

    @Test
    @Transactional
    @Rollback
    void getChatSubscriptions__hasZeroSubscriptions_emptyList() {
        // given
        Long chatId = 1L;
        helper.addChat(chatId);

        // when
        List<Link> chatSubscriptions = subscriptionService.getChatSubscriptions(chatId);

        // then
        assertTrue(chatSubscriptions.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void getChatSubscriptions__hasTwoSubscriptions_twoCorrectItems() {
        // given
        Long chatId = 1L;
        Long linkId1 = 1L;
        Long linkId2 = 2L;
        String url1 = "https://github.com/fenolmbyte/project-1/";
        String url2 = "https://github.com/fenolmbyte/project-2/";
        helper.addChat(chatId);
        helper.addLink(linkId1, url1);
        helper.addLink(linkId2, url2);
        helper.addSubscription(chatId, linkId1);
        helper.addSubscription(chatId, linkId2);

        // when
        List<Link> chatSubscriptions = subscriptionService.getChatSubscriptions(chatId);

        // then
        assertEquals(chatSubscriptions.size(), 2);
        assertEquals(chatSubscriptions.get(0).getId(), linkId1);
        assertEquals(chatSubscriptions.get(1).getId(), linkId2);
    }

    @Test
    @Transactional
    @Rollback
    void getLinkSubscribers__hasOneSubscriber_oneCorrectItem() {
        // given
        Long chatId = 1L;
        Long linkId = 1L;
        String url = "https://github.com/fenolmbyte/project/";
        helper.addChat(chatId);
        helper.addLink(linkId, url);
        helper.addSubscription(chatId, linkId);

        // when
        List<Chat> linkSubscribers = subscriptionService.getLinkSubscribers(linkId);

        // then
        assertEquals(linkSubscribers.size(), 1);
        assertEquals(linkSubscribers.get(0).getId(), chatId);
    }
}