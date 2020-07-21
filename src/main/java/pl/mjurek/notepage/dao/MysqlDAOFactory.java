package pl.mjurek.notepage.dao;

import pl.mjurek.notepage.dao.datenote.DateNoteDAO;
import pl.mjurek.notepage.dao.datenote.DateNoteDAOImpl;
import pl.mjurek.notepage.dao.key.KeyActionDAO;
import pl.mjurek.notepage.dao.key.KeyActionDAOImpl;
import pl.mjurek.notepage.dao.note.NoteDAO;
import pl.mjurek.notepage.dao.note.NoteDAOImpl;
import pl.mjurek.notepage.dao.user.UserDAO;
import pl.mjurek.notepage.dao.user.UserDAOImpl;


public class MysqlDAOFactory extends DAOFactory {
    @Override
    public UserDAO getUserDAO() {
        return new UserDAOImpl();
    }

    @Override
    public NoteDAO getNoteDAO() {
        return new NoteDAOImpl();
    }

    @Override
    public DateNoteDAO getDateNoteDAO() {
        return new DateNoteDAOImpl();
    }

    @Override
    public KeyActionDAO getKeyActionDAO() {
        return new KeyActionDAOImpl();
    }

}
