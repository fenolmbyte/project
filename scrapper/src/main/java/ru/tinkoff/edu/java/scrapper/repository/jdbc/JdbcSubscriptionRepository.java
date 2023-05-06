package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.dto.model.Subscription;

import java.util.List;

@RequiredArgsConstructor
public class JdbcSubscriptionRepository {
    private final JdbcTemplate template;
    private final BeanPropertyRowMapper<Subscription> mapper = new BeanPropertyRowMapper<>(Subscription.class);

    private final static String ADD_QUERY = "insert into subscription (chat_id, link_id) values (?, ?)";
    private final static String FIND_ALL_QUERY = "select chat_id, link_id from subscription";
    private final static String REMOVE_QUERY = "delete from subscription where chat_id = ? and link_id = ?";
    private final static String COUNT_SUBSCRIPTIONS_QUERY = "select count(chat_id) from subscription where link_id = ?";

    public Integer add(Long chatId, Long linkId) throws DuplicateKeyException {
        return template.update(ADD_QUERY, chatId, linkId);
    }

    public List<Subscription> findAll() {
        return template.query(FIND_ALL_QUERY, mapper);
    }

    public Integer remove(Long chatId, Long linkId) {
        return template.update(REMOVE_QUERY, chatId, linkId);
    }

    public Integer countSubscriptions(Long linkId) {
        return template.queryForObject(COUNT_SUBSCRIPTIONS_QUERY, Integer.class, linkId);
    }
}
