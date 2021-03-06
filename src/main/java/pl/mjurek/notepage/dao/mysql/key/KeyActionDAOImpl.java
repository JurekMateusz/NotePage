package pl.mjurek.notepage.dao.mysql.key;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.model.keyaction.KeyAction;
import pl.mjurek.notepage.util.ConnectionProvider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyActionDAOImpl implements KeyActionDAO {
  private static final String CREATE =
      "INSERT INTO key_action(user_id ,code) VALUES(:user_id, :code);";
  private static final String READ =
      "SELECT user_id ,code FROM key_action WHERE code = :code LIMIT 1;";
  private static final String DELETE = "DELETE FROM key_action WHERE code = :code;";

  private final NamedParameterJdbcTemplate template;

  public KeyActionDAOImpl() {
    template = new NamedParameterJdbcTemplate(ConnectionProvider.getDataSource());
  }

  @Override
  public KeyAction create(KeyAction key) throws AddObjectException {
    Map<String, Object> paramMap = new HashMap<>();
    paramMap.put("user_id", key.getUserId());
    paramMap.put("code", key.getKey());
    SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
    int update = template.update(CREATE, paramSource);
    if (update < 1) {
      String message =
          String.format(
              "Can't insert entity: %s  to database.Entity: %s", key.getClass(), key.toString());
      throw new AddObjectException(message);
    }
    return key;
  }

  @Override
  public KeyAction read(String key) {
    SqlParameterSource paramSource = new MapSqlParameterSource("code", key);
    List<KeyAction> result = template.query(READ, paramSource, new KeyActionRowMapper());
    return result.isEmpty() ? null : result.get(0);
  }

  @Override
  public void delete(String key) throws DeleteObjectException {
    MapSqlParameterSource paramSource = new MapSqlParameterSource("code", key);
    int update = template.update(DELETE, paramSource);
    if (update < 1) {
      throw new DeleteObjectException("Can't delete KeyAction entity.");
    }
  }

  private static class KeyActionRowMapper implements RowMapper<KeyAction> {
    @Override
    public KeyAction mapRow(ResultSet resultSet, int i) throws SQLException {
      return KeyAction.builder()
          .userId(resultSet.getLong("user_id"))
          .key(resultSet.getString("code"))
          .build();
    }
  }
}
