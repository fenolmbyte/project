package ru.tinkoff.edu.java.scrapper.service.domain.jpa;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.service.domain.api.ChatService;

@SpringBootTest
class JpaChatServiceTest extends IntegrationEnvironment {
    @Autowired
    private ChatService chatService;

    @Autowired
    private ServicesTestHelper helper;

    @Test
    @Transactional
    @Rollback
    void register__chatDoesNotExist_registersChat() {
        // given
        Long id = 1L;
        assertNull(helper.getChatById(id));

        // when
        chatService.register(id);

        // then
        assertNotNull(helper.getChatById(id));
    }

    @Test
    @Transactional
    @Rollback
    void register__chatExists_throwsException() {
        // given
        Long id = 1L;
        helper.addChat(id);

        // when

        // then
        assertNotNull(helper.getChatById(id));
        assertThrows(IllegalArgumentException.class, () -> chatService.register(id));
    }

    @Test
    @Transactional
    @Rollback
    void unregister__chatExists_unregistersChat() {
        // given
        Long id = 1L;
        helper.addChat(id);

        // when

        // then
        assertNotNull(helper.getChatById(id));
        chatService.unregister(id);
        assertNull(helper.getChatById(id));
    }

    @Test
    @Transactional
    @Rollback
    void unregister__chatDoesNotExist_throwsException() {
        // given
        Long id = 1L;

        // when

        // then
        assertNull(helper.getChatById(id));
        assertThrows(IllegalArgumentException.class, () -> chatService.unregister(id));
    }

    @Test
    @Transactional
    @Rollback
    void unregister__linkHasOnlySubscriber_deletesLink() {
        // given
        Long chatId = 1L;
        Long linkId = 1L;
        String url = "https://github.com/fenolmbyte/project/";
        helper.addChat(chatId);
        helper.addLink(linkId, url);
        helper.addSubscription(chatId, linkId);

        // when

        // then
        assertNotNull(helper.getChatById(chatId));
        assertNotNull(helper.getLinkById(linkId));
        assertNotNull(helper.getSubscriptionById(chatId, linkId));
        chatService.unregister(chatId);
        assertNull(helper.getChatById(chatId));
        assertNull(helper.getLinkById(linkId));
        assertNull(helper.getSubscriptionById(chatId, linkId));
    }
}
