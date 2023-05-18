package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import java.sql.PreparedStatement;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.tinkoff.edu.java.scrapper.dto.model.Link;

@Slf4j
@RequiredArgsConstructor
public class JdbcLinkRepository {
    private final JdbcTemplate template;
    private final BeanPropertyRowMapper<Link> mapper = new BeanPropertyRowMapper<>(Link.class);

    private final static String ADD_QUERY = "insert into link (url) values (?)";
    private final static String FIND_QUERY = """
        select id, url, last_check_time, last_update_time
        from link
        where url = ?
        """;
    private final static String FIND_BY_ID_QUERY = """
        select id, url, last_check_time, last_update_time
        from link
        where id = ?
        """;
    private final static String FIND_ALL_QUERY = "select id, url, last_check_time, last_update_time from link";
    private final static String FIND_WITH_SUBSCRIBER_QUERY = """
        select id, url, last_check_time, last_update_time
        from link
        join subscription s on link.id = s.link_id
        where chat_id = ?
            """;
    private final static String UPDATE_LAST_CHECKED_TIME_AND_GET = """
        update link
        set last_check_time = now()
        where ? > last_check_time
        returning id, url, last_check_time, last_update_time
        """;
    private final static String UPDATE_LAST_UPDATE_TIME_QUERY = "update link set last_update_time = ? where id = ?";
    private final static String REMOVE_QUERY = "delete from link where url = ?";
    private final static String REMOVE_BY_ID_QUERY = "delete from link where id = ?";
    private final static String REMOVE_WITH_ZERO_SUBSCRIBERS_QUERY = """
        delete from link
        where link.id in
        (select id from link
        left outer join subscription s on link.id = s.link_id
        where s.chat_id is NULL)
        """;

    public Long add(String url) throws DuplicateKeyException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(con -> {
            PreparedStatement ps = con.prepareStatement(ADD_QUERY, new String[] {"id"});
            ps.setString(1, url);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public Link find(String url) throws EmptyResultDataAccessException {
        return template.queryForObject(FIND_QUERY, mapper, url);
    }

    public Link findById(Long id) throws EmptyResultDataAccessException {
        return template.queryForObject(FIND_BY_ID_QUERY, mapper, id);
    }

    public List<Link> findAll() {
        return template.query(FIND_ALL_QUERY, mapper);
    }

    public List<Link> findWithSubscriber(Long chatId) {
        return template.query(FIND_WITH_SUBSCRIBER_QUERY, mapper, chatId);
    }

    public List<Link> updateLastCheckedTimeAndGet(OffsetDateTime shouldBeCheckedAfter) {
        return template.query(UPDATE_LAST_CHECKED_TIME_AND_GET, mapper, shouldBeCheckedAfter);
    }

    public Integer updateLastUpdateTime(Long id, OffsetDateTime newUpdateTime) {
        log.info(newUpdateTime.toString());
        return template.update(UPDATE_LAST_UPDATE_TIME_QUERY, newUpdateTime, id);
    }

    public Integer remove(String url) {
        return template.update(REMOVE_QUERY, url);
    }

    public Integer removeById(Long id) {
        return template.update(REMOVE_BY_ID_QUERY, id);
    }

    public Integer removeWithZeroSubscribers() {
        return template.update(REMOVE_WITH_ZERO_SUBSCRIBERS_QUERY);
    }
}
