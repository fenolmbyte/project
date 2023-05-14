package ru.tinkoff.edu.java.scrapper.service.domain.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.dto.model.Chat;
import ru.tinkoff.edu.java.scrapper.dto.model.Link;
import ru.tinkoff.edu.java.scrapper.dto.model.Subscription;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
public class ServicesTestHelper {
    private final static String ADD_CHAT_QUERY = "insert into chat (id) values (?)";
    private final static String ADD_LINK_QUERY = "insert into link (id, url) values (?, ?)";
    private final static String ADD_LINK_ALL_FIELDS_QUERY =
            "insert into link (id, url, last_check_time, last_update_time) values (?, ?, ?, ?)";
    private final static String ADD_SUBSCRIPTION_QUERY =
            "insert into subscription (chat_id, link_id) values (?, ?)";
    private final static String FIND_BY_ID_CHAT_QUERY =
            "select id from chat where id = ?";
    private final static String FIND_BY_ID_LINK_QUERY =
            "select id, url, last_check_time, last_update_time from link where id = ?";
    private final static String FIND_BY_ID_SUBSCRIPTION_QUERY =
            "select chat_id, link_id from subscription where chat_id = ? and link_id = ?";


    private final JdbcTemplate template;
    private final BeanPropertyRowMapper<Chat> chatMapper = new BeanPropertyRowMapper<>(Chat.class);
    private final BeanPropertyRowMapper<Link> linkMapper = new BeanPropertyRowMapper<>(Link.class);
    private final BeanPropertyRowMapper<Subscription> subscriptionMapper =
            new BeanPropertyRowMapper<>(Subscription.class);

    public void addChat(Long id) {
        template.update(ADD_CHAT_QUERY, id);
    }

    public void addLink(Long id, String url) {
        template.update(ADD_LINK_QUERY, id, url);
    }

    public void addLink(Long id, String url, OffsetDateTime lastCheckTime, OffsetDateTime lastUpdateTime) {
        template.update(ADD_LINK_ALL_FIELDS_QUERY, id, url, lastCheckTime, lastUpdateTime);
    }

    public void addSubscription(Long chatId, Long linkId) {
        template.update(ADD_SUBSCRIPTION_QUERY, chatId, linkId);
    }

    public Chat getChatById(Long id) {
        try {
            return template.queryForObject(FIND_BY_ID_CHAT_QUERY, chatMapper, id);
        } catch (EmptyResultDataAccessException ignored) {
            return null;
        }
    }

    public Link getLinkById(Long id) {
        try {
            return template.queryForObject(FIND_BY_ID_LINK_QUERY, linkMapper, id);
        } catch (EmptyResultDataAccessException ignored) {
            return null;
        }
    }

    public Subscription getSubscriptionById(Long chatId, Long linkId) {
        try {
            return template.queryForObject(FIND_BY_ID_SUBSCRIPTION_QUERY, subscriptionMapper, chatId, linkId);
        } catch (EmptyResultDataAccessException ignored) {
            return null;
        }
    }
}
