package pl.mjurek.notepage.dao;

import pl.mjurek.notepage.dao.datenote.DateNoteDAO;
import pl.mjurek.notepage.dao.note.NoteDAO;
import pl.mjurek.notepage.dao.user.UserDAO;

public abstract class DAOFactory {
    public abstract UserDAO getUserDAO();

    public abstract NoteDAO getNoteDAO();

    public abstract DateNoteDAO getDateNoteDAO();
    //TODO active_nowDAO ???

}
