package pl.mjurek.notepage.dao.user;

import pl.mjurek.notepage.dao.GenericDAO;
import pl.mjurek.notepage.model.User;

import java.util.List;

public interface UserDAO extends GenericDAO<User, Long> {
    //List<User> getAllActiveNow();
    User getUserByUserName(String userName);

    boolean isNotExit(String userName);
}
