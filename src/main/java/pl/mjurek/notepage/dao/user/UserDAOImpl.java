package pl.mjurek.notepage.dao.user;

import org.springframework.core.env.MapPropertySource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.Note;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.util.ConnectionProvider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAOImpl implements UserDAO {

    private static final String CREATE =
            "INSERT INTO user(name,email,password) VALUES (:name,:email,:password);";

    private static final String UPDATE =
            "UPDATE user SET email=:email, password=:password WHERE user_id=:user_id;";
    private static final String UPDATE_VERIFICATION =
            "UPDATE user SET verification=:verification WHERE user_id=:user_id;";
    private static final String READ_USER_BY_USERNAME =
            "SELECT user_id,name,email,password,verification FROM user WHERE name=:name LIMIT 1;";

    private static final String READ_USER_BY_EMAIL =
            "SELECT user_id,name,email,password,verification FROM user WHERE email=:email LIMIT 1;";
    private static final String DELETE =
            "DELETE FROM user WHERE user_id=:user_id";
    private NamedParameterJdbcTemplate template;

    public UserDAOImpl() {
        template = new NamedParameterJdbcTemplate(ConnectionProvider.getDataSource());
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        int update = template.update(CREATE, parameterSource, keyHolder);
        User resultUser = copyIfUpdateSuccessful(update, user, keyHolder);
        return resultUser;
    }

    private User copyIfUpdateSuccessful(int update, User user, KeyHolder keyHolder) {
        User resultUser = null;
        if (update > 0) {
            resultUser = User.builder()
                    .id(keyHolder.getKey().longValue())
                    .name(user.getName())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .build();
        }
        return resultUser;
    }


    @Override
    public User read(Long primaryKey) {
        return null;
    }

    @Override
    public User update(User updateUser) throws UpdateObjectException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("email", updateUser.getEmail());
        paramMap.put("password", updateUser.getPassword());
        paramMap.put("user_id", updateUser.getId());
        SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
        int update = template.update(UPDATE, paramSource);
        if (update < 1) {
            throw new UpdateObjectException();
        }
        return updateUser;
    }


    @Override
    public void delete(Long userId) throws DeleteObjectException {
        MapSqlParameterSource paramSource = new MapSqlParameterSource("user_id", userId);
        int update = template.update(DELETE, paramSource);
        if (update < 1) {
            throw new DeleteObjectException();
        }
    }

    @Override
    public User getUserByUserName(String username) {
        SqlParameterSource parameterSource = new MapSqlParameterSource("name", username);
        List<User> resultUser = template.query(READ_USER_BY_USERNAME, parameterSource, new UserRowMapper());
        if (resultUser.isEmpty()) {
            return null;
        }
        return resultUser.get(0);
    }

    @Override
    public User getUserByEmail(String email) {
        SqlParameterSource parameterSource = new MapSqlParameterSource("email", email);
        List<User> resultUser = template.query(READ_USER_BY_EMAIL, parameterSource, new UserRowMapper());
        if (resultUser.isEmpty()) {
            return null;
        }
        return resultUser.get(0);
    }

    @Override
    public void updateVerification(long userId, String status) throws UpdateObjectException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("user_id", userId);
        paramMap.put("verification", status);
        SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
        int update = template.update(UPDATE_VERIFICATION, paramSource);
        if (update < 1) {
            throw new UpdateObjectException();
        }
    }


    private class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            Long id = resultSet.getLong("user_id");
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
