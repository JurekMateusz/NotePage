package pl.mjurek.notepage.dao.user;

import pl.mjurek.notepage.dao.GenericDAO;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.User;

import java.util.Optional;


public interface UserDAO extends GenericDAO<User, Long> {
    User getUserByUserName(String username);

    Optional<User> readUserByCredential(String name, String password);

    Optional<User> readUserByEmail(String email);

    void updatePassword(long id, String password) throws UpdateObjectException;

    void updateVerification(long userId, String status) throws UpdateObjectException;

    boolean isUsernameExist(String username);

    boolean isEmailExist(String email);
}
