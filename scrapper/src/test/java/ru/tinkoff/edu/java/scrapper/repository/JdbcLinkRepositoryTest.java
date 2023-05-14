package ru.tinkoff.edu.java.scrapper.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.dto.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;

import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class JdbcLinkRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcTemplate template;

    @Autowired
    private JdbcLinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    void add__addOne_oneAdded() {
        // given
        String url = "https://github.com/fenolmbyte/project";

        // when
        int countBefore = getAll().size();
        linkRepository.add(url);
        int countAfter = getAll().size();

        // then
        assertEquals(countBefore + 1, countAfter);
    }

    @Test
    @Transactional
    @Rollback
    void add__alreadyExist_throwsException() {
        // given
        String url = "https://github.com/fenolmbyte/project";

        // when
        linkRepository.add(url);

        // then
        assertThrows(DuplicateKeyException.class, () -> linkRepository.add(url));
    }

    @Test
    @Transactional
    @Rollback
    void find__exists_returnsOne() {
        // given
        String url = "https://github.com/fenolmbyte/project";
        Long id = createLink(url);

        // when
        Link link = linkRepository.find(url);

        // then
        assertEquals(link.getId(), id);
    }

    @Test
    @Transactional
    @Rollback
    void find__notExists_throwsException() {
        // given
        String url = "https://github.com/fenolmbyte/project";

        // when

        // then
        assertThrows(EmptyResultDataAccessException.class, () -> linkRepository.find(url));
    }

    @Test
    @Transactional
    @Rollback
    void findById__exists_returnsOne() {
        // given
        String url = "https://github.com/fenolmbyte/project";
        Long id = createLink(url);

        // when
        Link link = linkRepository.findById(id);

        // then
        assertEquals(link.getId(), id);
    }

    @Test
    @Transactional
    @Rollback
    void findById__notExists_throwsException() {
        // given
        Long id = 1L;

        // when

        // then
        assertThrows(EmptyResultDataAccessException.class, () -> linkRepository.findById(id));
    }

    @Test
    @Transactional
    @Rollback
    void findAll__nothingExists_zeroItemsReturned() {
        // given

        // when
        List<Link> all = linkRepository.findAll();

        // then
        assertEquals(all.size(), 0);
    }

    @Test
    @Transactional
    @Rollback
    void findAll__oneExists_oneReturned() {
        // given
        String url = "https://github.com/fenolmbyte/project";
        createLink(url);

        // when
        List<Link> all = linkRepository.findAll();

        // then
        assertEquals(all.size(), 1);
    }

    @Test
    @Transactional
    @Rollback
    void findWithChatSubscription__exists_returnsNotEmpty() {
        // given
        String url = "https://github.com/fenolmbyte/project";
        Long chatId = 1L;
        Long linkId = createLink(url);
        createChat(chatId);
        createSubscription(chatId, linkId);

        // when
        List<Link> linkEntities = linkRepository.findWithSubscriber(chatId);

        // then
        assertEquals(linkEntities.size(), 1);
    }

    @Test
    @Transactional
    @Rollback
    void findWithChatSubscription__notExists_returnsEmpty() {
        // given
        Long chatId = 1L;

        // when
        List<Link> linkEntities = linkRepository.findWithSubscriber(chatId);

        // then
        assertEquals(linkEntities.size(), 0);
    }

    @Test
    @Transactional
    @Rollback
    void remove__oneExists_oneRemoved() {
        // given
        String url = "https://github.com/fenolmbyte/project";
        createLink(url);

        // when
        int countBefore = getAll().size();
        int removed = linkRepository.remove(url);
        int countAfter = getAll().size();

        // then
        assertEquals(removed, 1);
        assertEquals(countBefore - 1, countAfter);
    }

    @Test
    @Transactional
    @Rollback
    void remove__notExists_zeroRemoved() {
        // given
        String url = "https://github.com/fenolmbyte/project";

        // when
        int countBefore = getAll().size();
        int removed = linkRepository.remove(url);
        int countAfter = getAll().size();

        // then
        assertEquals(removed, 0);
        assertEquals(countBefore, countAfter);
    }

    @Test
    @Transactional
    @Rollback
    void removeById__exists_removed() {
        // given
        String url = "https://github.com/fenolmbyte/project";
        Long id = createLink(url);

        // when
        int beforeCount = getAll().size();
        linkRepository.removeById(id);
        int afterCount = getAll().size();

        // then
        assertEquals(beforeCount - 1, afterCount);
    }

    @Test
    @Transactional
    @Rollback
    void removeById__notExists_notRemoved() {
        // given
        Long id = 1L;

        // when
        int beforeCount = getAll().size();
        linkRepository.removeById(id);
        int afterCount = getAll().size();

        // then
        assertEquals(beforeCount, afterCount);
    }

    @Test
    @Transactional
    @Rollback
    void removeWithZeroSubscriptions__doesNotHaveSub_removed() {
        // given
        String url = "https://github.com/fenolmbyte/project";
        createLink(url);

        // when
        int beforeCount = getAll().size();
        int removed = linkRepository.removeWithZeroSubscribers();
        int afterCount = getAll().size();

        // then
        assertEquals(removed, 1);
        assertEquals(beforeCount - 1, afterCount);
    }

    @Test
    @Transactional
    @Rollback
    void removeWithZeroSubscriptions__allLinksHaveSub_nothingRemoved() {
        // given
        String url = "https://github.com/fenolmbyte/project";
        Long chatId = 1L;
        Long linkId = createLink(url);
        createChat(chatId);
        createSubscription(chatId, linkId);

        // when
        int beforeCount = getAll().size();
        int removed = linkRepository.removeWithZeroSubscribers();
        int afterCount = getAll().size();

        // then
        assertEquals(removed, 0);
        assertEquals(beforeCount, afterCount);
    }

    private List<Link> getAll() {
        return template.query("select id, url from link", new BeanPropertyRowMapper<>(Link.class));
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