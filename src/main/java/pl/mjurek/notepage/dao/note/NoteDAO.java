package pl.mjurek.notepage.dao.note;

import pl.mjurek.notepage.dao.GenericDAO;
import pl.mjurek.notepage.model.Note;

import java.util.List;

public interface NoteDAO extends GenericDAO<Note,Long> {
    List<Note> getAll();
    //TODO getAllBy sort option ,getActualNote(),getOldNote(),getMadeNote(),getNotMadeNote();
}
