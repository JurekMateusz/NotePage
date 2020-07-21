package pl.mjurek.notepage.dao;

import pl.mjurek.notepage.dao.datenote.DateNoteDAO;
import pl.mjurek.notepage.dao.key.KeyActionDAO;
import pl.mjurek.notepage.dao.note.NoteDAO;
import pl.mjurek.notepage.dao.user.UserDAO;
import pl.mjurek.notepage.exception.NoSuchDbTypeException;

public abstract class DAOFactory {
    public static final int MYSQL_DAO_FACTORY = 1;

    public static DAOFactory getDAOFactory() {
        DAOFactory factory = null;
        try {
            factory = getDAOFactory(MYSQL_DAO_FACTORY);
        } catch (NoSuchDbTypeException e) {
            e.printStackTrace();
        }
        return factory;
    }

    public static DAOFactory getDAOFactory(int type) throws NoSuchDbTypeException {
        switch (type) {
            case MYSQL_DAO_FACTORY:
                return new MysqlDAOFactory();
            default:
                throw new NoSuchDbTypeException();
        }
    }

    public abstract UserDAO getUserDAO();

    public abstract NoteDAO getNoteDAO();

    public abstract DateNoteDAO getDateNoteDAO();

    public abstract KeyActionDAO getKeyActionDAO();
}
