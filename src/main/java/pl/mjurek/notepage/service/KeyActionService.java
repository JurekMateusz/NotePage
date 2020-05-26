package pl.mjurek.notepage.service;

import pl.mjurek.notepage.dao.DAOFactory;
import pl.mjurek.notepage.dao.key.KeyActionDAO;
import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.model.KeyAction;

public class KeyActionService {
    public void addKey(long userId, String key) {
        KeyActionDAO keyDAO = getKeyActionDAO();
        KeyAction keyAction = KeyAction.builder()
                .userId(userId)
                .key(key)
                .build();
        try {
            keyDAO.create(keyAction);
        } catch (AddObjectException e) {
            e.printStackTrace();
        }
    }

    public KeyAction read(String key) {
        KeyActionDAO keyActionDAO = getKeyActionDAO();
        KeyAction keyAction = keyActionDAO.read(key);
        return keyAction;
    }

    public void delete(long userId) {
        KeyActionDAO actionDAO = getKeyActionDAO();
        try {
            actionDAO.delete(userId);
        } catch (DeleteObjectException e) {
            e.printStackTrace();
        }
    }

    private KeyActionDAO getKeyActionDAO() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        return factory.getKeyActionDAO();
    }
}
