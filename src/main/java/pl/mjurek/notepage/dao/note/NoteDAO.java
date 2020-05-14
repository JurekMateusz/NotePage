package pl.mjurek.notepage.dao.note;

import pl.mjurek.notepage.dao.GenericDAO;
import pl.mjurek.notepage.model.ImportantState;
import pl.mjurek.notepage.model.Note;
import pl.mjurek.notepage.model.StatusNote;

import java.util.List;

public interface NoteDAO extends GenericDAO<Note,Long> {
    List<Note> getAll(long user_id);
    List<Note> getAll(long user_id, StatusNote state);

    //TODO getAllBy sort option ,getActualNote(),getOldNote(),getMadeNote(),getNotMadeNote();
}
