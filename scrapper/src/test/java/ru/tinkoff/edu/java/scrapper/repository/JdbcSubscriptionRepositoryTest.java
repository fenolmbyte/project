package ru.tinkoff.edu.java.scrapper.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.dto.model.Subscription;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcSubscriptionRepository;

import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JdbcSubscriptionRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcTemplate template;

    @Autowired
    private JdbcSubscriptionRepository subscriptionRepository;

    @Test
    @Transactional
    @Rollback
    void add__addOne_oneAdded() {
        // given
        String url = "https://github.com/fenolmbyte/project";
        Long chatId = 1L;
        Long linkId = createLink(url);
        createChat(chatId);

        // when
        int beforeCount = getAll().size();
        subscriptionRepository.add(chatId, linkId);
        int afterCount = getAll().size();

        // then
        assertEquals(beforeCount + 1, afterCount);
    }

    @Test
    @Transactional
    @Rollback
    void add__alreadyExist_throwsException() {
        // given
        String url = "https://github.com/fenolmbyte/project";
        Long chatId = 1L;
        Long linkId = createLink(url);
        createChat(chatId);

        // when
        subscriptionRepository.add(chatId, linkId);

        // then
        assertThrows(DuplicateKeyException.class, () -> subscriptionRepository.add(chatId, linkId));
    }

    @Test
    @Transactional
    @Rollback
    void findAll__nothingExists_zeroItemsReturned() {
        // given

        // when
        int querySize = getAll().size();
        List<Subscription> all = subscriptionRepository.findAll();

        // then
        assertEquals(all.size(), 0);
        assertEquals(all.size(), querySize);
    }

    @Test
    @Transactional
    @Rollback
    void findAll__oneExists_oneReturned() {
        // given
        String url = "https://github.com/fenolmbyte/project";
        Long chatId = 1L;
        Long linkId = createLink(url);
        createChat(chatId);
        createSubscription(chatId, linkId);

        // when
        int querySize = getAll().size();
        List<Subscription> all = subscriptionRepository.findAll();

        // then
        assertEquals(all.size(), 1);
        assertEquals(all.size(), querySize);
    }

    @Test
    @Transactional
    @Rollback
    void remove__oneExists_oneRemoved() {
        // given
        String url = "https://github.com/fenolmbyte/project";
        Long chatId = 1L;
        Long linkId = createLink(url);
        createChat(chatId);
        createSubscription(chatId, linkId);

        // when
        int beforeCount = getAll().size();
        int removed = subscriptionRepository.remove(chatId, linkId);
        int afterCount = getAll().size();

        // then
        assertEquals(beforeCount - 1, afterCount);
        assertEquals(removed, 1);
    }

    @Test
    @Transactional
    @Rollback
    void remove__notExists_zeroRemoved() {
        // given
        Long chatId = 1L;
        Long linkId = 1L;

        // when
        int beforeCount = getAll().size();
        int removed = subscriptionRepository.remove(chatId, linkId);
        int afterCount = getAll().size();

        // then
        assertEquals(beforeCount, afterCount);
        assertEquals(removed, 0);
    }

    @Test
    @Transactional
    @Rollback
    void countSubscriptions__oneSubscriber_oneCounted() {
        // given
        String url = "https://github.com/fenolmbyte/project";
        Long chatId = 1L;
        Long linkId = createLink(url);
        createChat(chatId);
        createSubscription(chatId, linkId);

        // when
        Integer counted = subscriptionRepository.countSubscriptions(linkId);

        // then
        assertEquals(counted, 1);
    }

    @Test
    @Transactional
    @Rollback
    void countSubscriptions__zeroSubscribers_zeroCounted() {
        // given
        String url = "https://github.com/fenolmbyte/project";
        Long chatId = 1L;
        Long linkId = createLink(url);
        createChat(chatId);

        // when
        Integer counted = subscriptionRepository.countSubscriptions(linkId);

        // then
        assertEquals(counted, 0);
    }

    private List<Subscription> getAll() {
        return template.query("select chat_id, link_id from subscription", new BeanPropertyRowMapper<>(Subscription.class));
    }

    private Long createLink(String url) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(con -> {
            PreparedStatement ps = con.prepareStatement("insert into link (url) values (?)", new String[] {"id"});
            ps.setString(1, url);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    private void createChat(Long id) {
        template.update("insert into chat (id) values (?)", id);
    }

    private void createSubscription(Long chatId, Long linkId) {
        template.update("insert into subscription (chat_id, link_id) values (?, ?)", chatId, linkId);
    }
}