package pl.mjurek.notepage.service;

import pl.mjurek.notepage.dao.DAOFactory;
import pl.mjurek.notepage.dao.key.KeyActionDAO;
import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.model.KeyAction;

import java.util.Optional;

public class KeyActionService {
    public void addKey(long userId, String key) throws AddObjectException {
        KeyActionDAO keyDAO = getKeyActionDAO();
        KeyAction keyAction = KeyAction.builder()
                .userId(userId)
                .key(key)
                .build();

        keyDAO.create(keyAction);
    }

    public Optional<KeyAction> read(String key) {
        KeyActionDAO keyActionDAO = getKeyActionDAO();
        return Optional.ofNullable(keyActionDAO.read(key));
    }

    public void delete(String key) throws DeleteObjectException {
        KeyActionDAO actionDAO = getKeyActionDAO();

        actionDAO.delete(key);
    }

    private KeyActionDAO getKeyActionDAO() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        return factory.getKeyActionDAO();
    }
}
