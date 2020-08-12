package pl.mjurek.notepage.dao.mysql.user;

import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.user.User;

import java.util.Optional;

public interface UserDAO {
  User create(User user) throws AddObjectException;

  User update(User user) throws UpdateObjectException;

  void delete(long id) throws DeleteObjectException;

  User getUserByUserName(String username);

  Optional<User> readUserByCredential(String name, String password);

  Optional<User> readUserByEmail(String email);

  void updatePassword(long id, String password) throws UpdateObjectException;

  void updateVerification(long userId, String status) throws UpdateObjectException;

  boolean isUsernameOrEmailExist(String username,String email);
}
