package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.dto.model.Chat;

@RequiredArgsConstructor
public class JdbcChatRepository {
    private final JdbcTemplate template;
    private final BeanPropertyRowMapper<Chat> mapper = new BeanPropertyRowMapper<>(Chat.class);

    private final static String ADD_QUERY = "insert into chat (id) values (?)";
    private final static String REMOVE_BY_ID_QUERY = "delete from chat where id = ?";
    private final static String FIND_ALL_QUERY = "select id from chat";
    private final static String FIND_ALL_SUBSCRIBERS_QUERY = """
        select id
        from chat
        join subscription on chat.id = chat_id
        where link_id = ?
        """;

    public Integer add(Long id) throws DuplicateKeyException {
        return template.update(ADD_QUERY, id);
    }

    public Integer removeById(Long id) {
        return template.update(REMOVE_BY_ID_QUERY, id);
    }

    public List<Chat> findAll() {
        return template.query(FIND_ALL_QUERY, mapper);
    }

    public List<Chat> findAllSubscribers(Long linkId) {
        return template.query(FIND_ALL_SUBSCRIBERS_QUERY, mapper, linkId);
    }
}
