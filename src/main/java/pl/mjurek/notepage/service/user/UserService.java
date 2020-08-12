package pl.mjurek.notepage.service.user;

import pl.mjurek.notepage.dao.DAOFactory;
import pl.mjurek.notepage.dao.mysql.user.UserDAO;
import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.user.User;
import pl.mjurek.notepage.service.function.key.Hash;

import java.util.Optional;

public class UserService {
  private final UserDAO userDAO;

  public UserService() {
    DAOFactory factory = DAOFactory.getDAOFactory();
    userDAO = factory.getUserDAO();
  }

  public User addUser(User user) throws AddObjectException {
    user.setPassword(hashPassword(user.getPassword()));
    return userDAO.create(user);
  }

  public User getUserByUserName(String name) {
    return userDAO.getUserByUserName(name);
  }

  public Optional<User> getUserByCredential(String name, String password) {
    password = hashPassword(password);
    return userDAO.readUserByCredential(name, password);
  }

  public User update(User user) throws UpdateObjectException {
    user.setPassword(hashPassword(user.getPassword()));
    return userDAO.update(user);
  }

  public void updatePassword(long id, String password) throws UpdateObjectException {
    password = hashPassword(password);
    userDAO.updatePassword(id, password);
  }

  public Optional<User> getUserByEmail(String email) {
    return userDAO.readUserByEmail(email);
  }

  public void delete(long id) throws DeleteObjectException {
    userDAO.delete(id);
  }

  public void updateVerification(long userId, String status) {
    try {
      userDAO.updateVerification(userId, status);
    } catch (UpdateObjectException e) {
      e.printStackTrace();
    }
  }

  public boolean isNameOrEmailExisting(String name, String email) {
    return userDAO.isUsernameOrEmailExist(name, email);
  }

  public void unblock(long userId) throws UpdateObjectException {
    userDAO.updateVerification(userId, "YES");
  }

  private String hashPassword(String password) {
    return Hash.encodePassword(password);
  }
}
