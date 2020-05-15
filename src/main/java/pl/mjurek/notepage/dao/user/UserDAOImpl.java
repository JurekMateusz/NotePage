package pl.mjurek.notepage.dao.user;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.util.ConnectionProvider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private static final String CREATE =
            "INSERT INTO user(user_name,email,password) VALUES (:name,:email,:password);";

///////// rest MAIN queries.

    private static final String READ_USER_BY_USERNAME =
            "SELECT user_id,user_name,email,password FROM user WHERE user_name=:name LIMIT 1;";

    private static final String READ_USER_BY_EMAIL =
            "SELECT user_id,user_name,email,password FROM user WHERE email=:email LIMIT 1;";

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
    public User update(User updateObject) {
        return null;
    }

    @Override
    public void delete(Long key) {

    }

    @Override
    public User getUserByUserName(String userName) {
        SqlParameterSource parameterSource = new MapSqlParameterSource("name", userName);
        List<User> resultUser = (List<User>) template.query(READ_USER_BY_USERNAME, parameterSource, new UserRowMapper());
        if (resultUser.isEmpty()) {
            return null;
        }
        return resultUser.get(0);
    }

    @Override
    public User getUserByEmail(String email) {
        SqlParameterSource parameterSource = new MapSqlParameterSource("email", email);
        List<User> resultUser = (List<User>) template.query(READ_USER_BY_EMAIL, parameterSource, new UserRowMapper());
        if (resultUser.isEmpty()) {
            return null;
        }
        return resultUser.get(0);
    }

    private class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = null;
            Long id = resultSet.getLong("user_id");
            String name = resultSet.getString("user_name");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");

            if (isAllNotNull(id, name, email, password)) {
                user = User.builder()
                        .id(id)
                        .name(name)
                        .email(email)
                        .password(password)
                        .build();
            }
            return user;
        }

        private boolean isAllNotNull(Object... args) {
            for (Object object : args) {
                if (object == null) {
                    return false;
                }
            }
            return true;
        }
    }
}
