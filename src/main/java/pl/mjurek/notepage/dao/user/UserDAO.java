package pl.mjurek.notepage.dao.user;

import pl.mjurek.notepage.dao.GenericDAO;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.User;


public interface UserDAO extends GenericDAO<User, Long> {
    User getUserByUserName(String username);

    User getUserByEmail(String email);

    void updateVerification(long userId, String status) throws UpdateObjectException;

    boolean isUsernameExist(String username);

    boolean isEmailExist(String email);
}
