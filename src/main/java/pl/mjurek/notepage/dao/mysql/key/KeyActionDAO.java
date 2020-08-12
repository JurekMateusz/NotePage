package pl.mjurek.notepage.dao.mysql.key;

import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.model.keyaction.KeyAction;

public interface KeyActionDAO {
  KeyAction create(KeyAction key) throws AddObjectException;

  KeyAction read(String key);

  void delete(String key) throws DeleteObjectException;
}
