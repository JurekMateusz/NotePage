package pl.mjurek.notepage.dao.key;

import pl.mjurek.notepage.dao.GenericDAO;
import pl.mjurek.notepage.model.KeyAction;


public interface KeyActionDAO extends GenericDAO<KeyAction,Long> {
    KeyAction read(String key);
}
