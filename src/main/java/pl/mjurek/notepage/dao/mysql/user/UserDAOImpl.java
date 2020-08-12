package pl.mjurek.notepage.dao.mysql.user;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.user.User;
import pl.mjurek.notepage.util.ConnectionProvider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {

  private static final String CREATE =
      "INSERT INTO user(name, email, password) VALUES (:name, :email, :password);";

  private static final String UPDATE =
      "UPDATE user SET email = :email, password = :password WHERE user_id = :user_id;";
  private static final String UPDATE_PASSWORD =
      "UPDATE user SET password = :password WHERE user_id = :user_id;";

  private static final String UPDATE_VERIFICATION =
      "UPDATE user SET verification = :verification WHERE user_id = :user_id;";

  private static final String READ_USER_BY_USERNAME =
      "SELECT user_id ,name ,email ,password ,verification FROM user WHERE name = :name LIMIT 1;";

  private static final String READ_USER_BY_CREDENTIAL =
      "SELECT user_id ,name ,email ,password ,verification FROM user "
          + "WHERE name = :name AND password = :password AND verification = 'YES' LIMIT 1;";

  private static final String READ_USER_BY_EMAIL =
      "SELECT user_id ,name ,email ,password ,verification FROM user WHERE email = :email LIMIT 1;";

  private static final String DELETE = "DELETE FROM user WHERE user_id = :user_id";

  private final String IS_NAME_OR_EMAIL_EXIST =
      "SELECT EXISTS(SELECT 1 FROM user WHERE name = :name OR email = :email )";

  private final NamedParameterJdbcTemplate template;

  public UserDAOImpl() {
    template = new NamedParameterJdbcTemplate(ConnectionProvider.getDataSource());
  }

  @Override
  public User create(User user) throws AddObjectException {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
    int update = template.update(CREATE, parameterSource, keyHolder);
    if (update == 0) {
      String message =
          String.format(
              "Can't insert entity: %s  to database.Entity: %s", user.getClass(), user.toString());
      throw new AddObjectException(message);
    }
    return copyUser(update, user, keyHolder);
  }

  private User copyUser(int update, User user, KeyHolder keyHolder) {
    return User.builder()
        .id(keyHolder.getKey().longValue())
        .name(user.getName())
        .email(user.getEmail())
        .password(user.getPassword())
        .build();
  }

  @Override
  public Optional<User> readUserByCredential(String name, String password) {
    Map<String, Object> paramMap = new HashMap<>();
    paramMap.put("name", name);
    paramMap.put("password", password);
    SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);

    List<User> users = template.query(READ_USER_BY_CREDENTIAL, paramSource, new UserRowMapper());
    return users.isEmpty() ? Optional.empty() : Optional.ofNullable(users.get(0));
  }

  @Override
  public Optional<User> readUserByEmail(String email) {
    SqlParameterSource parameterSource = new MapSqlParameterSource("email", email);
    List<User> users = template.query(READ_USER_BY_EMAIL, parameterSource, new UserRowMapper());

    return users.isEmpty() ? Optional.empty() : Optional.ofNullable(users.get(0));
  }

  @Override
  public void updatePassword(long id, String password) throws UpdateObjectException {
    Map<String, Object> paramMap = new HashMap<>();
    paramMap.put("password", password);
    paramMap.put("user_id", id);
    SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);

    int update = template.update(UPDATE_PASSWORD, paramSource);
    if (update < 1) {
      throw new UpdateObjectException("Can't update entity. Id: " + id);
    }
  }

  @Override
  public User update(User user) throws UpdateObjectException {
    Map<String, Object> paramMap = new HashMap<>();
    paramMap.put("email", user.getEmail());
    paramMap.put("password", user.getPassword());
    paramMap.put("user_id", user.getId());
    SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
    int update = template.update(UPDATE, paramSource);
    if (update < 1) {
      String message =
          String.format(
              "Can't update entity: %s  to database.Entity: %s", user.getClass(), user.toString());
      throw new UpdateObjectException(message);
    }
    return user;
  }

  @Override
  public void delete(long id) throws DeleteObjectException {
    MapSqlParameterSource paramSource = new MapSqlParameterSource("user_id", id);
    int update = template.update(DELETE, paramSource);
    if (update < 1) {
      throw new DeleteObjectException("Can't delete user entity. ID: " + id);
    }
  }

  @Override
  public User getUserByUserName(String username) {
    SqlParameterSource parameterSource = new MapSqlParameterSource("name", username);
    List<User> resultUser =
        template.query(READ_USER_BY_USERNAME, parameterSource, new UserRowMapper());

    if (resultUser.isEmpty()) {
      return null;
    }
    return resultUser.get(0);
  }

  @Override
  public void updateVerification(long id, String status) throws UpdateObjectException {
    Map<String, Object> paramMap = new HashMap<>();
    paramMap.put("user_id", id);
    paramMap.put("verification", status);
    SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
    int update = template.update(UPDATE_VERIFICATION, paramSource);
    if (update < 1) {
      throw new UpdateObjectException("Can't update user entity.ID: " + id);
    }
  }

  @Override
  public boolean isUsernameOrEmailExist(String username,String email) {
    Map<String, Object> paramMap = new HashMap<>();
    paramMap.put("name", username);
    paramMap.put("email", email);

    MapSqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
    int result = template.queryForObject(IS_NAME_OR_EMAIL_EXIST, paramSource, Integer.class);
    return result == 1;
  }

  private static class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
      long id = resultSet.getLong("user_id");
      String name = resultSet.getString("name");
      String email = resultSet.getString("email");
      String password = resultSet.getString("password");
      String verification = resultSet.getString("verification");

      return User.builder()
          .id(id)
          .name(name)
          .email(email)
          .password(password)
          .verification(verification)
          .build();
    }
  }
}
