package pl.mjurek.notepage.service.key;

import pl.mjurek.notepage.dao.DAOFactory;
import pl.mjurek.notepage.dao.mysql.key.KeyActionDAO;
import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.model.keyaction.KeyAction;

import java.util.Optional;

public class KeyActionService {
  private final KeyActionDAO keyActionDAO;

  public KeyActionService() {
    DAOFactory factory = DAOFactory.getDAOFactory();
    keyActionDAO = factory.getKeyActionDAO();
  }

  public void addKey(long userId, String key) throws AddObjectException {
    KeyAction keyAction = KeyAction.builder().userId(userId).key(key).build();
    keyActionDAO.create(keyAction);
  }

  public Optional<KeyAction> read(String key) {
    return Optional.ofNullable(keyActionDAO.read(key));
  }

  public void delete(String key) throws DeleteObjectException {
    keyActionDAO.delete(key);
  }
}
