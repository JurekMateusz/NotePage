package pl.mjurek.notepage.dao.mysql.note;

import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.note.Note;
import pl.mjurek.notepage.service.note.SortOptions;
import pl.mjurek.notepage.model.note.StatusNote;

import java.util.List;

public interface NoteDAO {
  Note create(Note note) throws AddObjectException;

  Note read(Long noteId);

  Note update(Note note) throws UpdateObjectException;

  void delete(Long id) throws DeleteObjectException;

  Note update(long dateId, String description, String importantStatus) throws UpdateObjectException;

  List<Note> getAll(long user_id);

  List<Note> getAll(long user_id, StatusNote state);

  List<Note> getAll(long user_id, StatusNote state, SortOptions orderBy);

  List<Note> getAll(long user_id, SortOptions orderBy);
}
