package pl.mjurek.notepage.dao.datenote;

import pl.mjurek.notepage.dao.GenericDAO;

import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.DateNote;

import java.sql.Timestamp;

public interface DateNoteDAO extends GenericDAO<DateNote,Long> {
    void update(long dateId, Timestamp deadline) throws UpdateObjectException;
}
