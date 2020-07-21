package pl.mjurek.notepage.dao.key;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.model.KeyAction;
import pl.mjurek.notepage.util.ConnectionProvider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class KeyActionDAOImpl implements KeyActionDAO {
    private static final String CREATE =
            "INSERT INTO key_action(user_id,code) VALUES(:user_id,:code);";
    private static final String READ =
            "SELECT user_id,code FROM key_action WHERE code=:code";
    private static final String DELETE =
            "DELETE FROM key_action WHERE user_id=:user_id;";

    private final NamedParameterJdbcTemplate template;

    public KeyActionDAOImpl() {
        template = new NamedParameterJdbcTemplate(ConnectionProvider.getDataSource());
    }

    @Override
    public KeyAction create(KeyAction key) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("user_id", key.getUserId());
        paramMap.put("code", key.getKey());
        SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
        int update = template.update(CREATE, paramSource);
        if (update < 1) {
            return null;
        }
        return key;
    }

    @Override
    public KeyAction read(Long userId) {
        return null;
    }

    @Override
    public KeyAction read(String key) {
        SqlParameterSource paramSource = new MapSqlParameterSource("code", key);
        KeyAction keyAction = template.queryForObject(READ, paramSource, new KeyActionDAOImpl.KeyActionRowMapper());
        return keyAction;
    }

    @Override
    public KeyAction update(KeyAction updateObject) {
        return null;
    }

    @Override
    public void delete(Long userId) throws DeleteObjectException {
        MapSqlParameterSource paramSource = new MapSqlParameterSource("user_id", userId);
        int update = template.update(DELETE, paramSource);
        if (update < 1) {
            throw new DeleteObjectException();
        }
    }

    private class KeyActionRowMapper implements RowMapper<KeyAction> {
        @Override
        public KeyAction mapRow(ResultSet resultSet, int i) throws SQLException {
            return KeyAction.builder()
                    .userId(resultSet.getLong("user_id"))
                    .key(resultSet.getString("code"))
                    .build();
        }
    }
}
