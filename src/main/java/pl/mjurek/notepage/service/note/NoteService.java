package pl.mjurek.notepage.service.note;

import pl.mjurek.notepage.dao.DAOFactory;
import pl.mjurek.notepage.dao.mysql.note.NoteDAO;
import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DataAccessException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.datenote.DateNote;
import pl.mjurek.notepage.model.note.Note;
import pl.mjurek.notepage.model.note.ImportantState;
import pl.mjurek.notepage.model.note.StatusNote;
import pl.mjurek.notepage.model.user.User;
import pl.mjurek.notepage.service.date.DateNoteService;
import pl.mjurek.notepage.service.function.encoding.NoteContentEncoder;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NoteService {
  private final NoteDAO noteDAO;

  public NoteService() {
    DAOFactory factory = DAOFactory.getDAOFactory();
    noteDAO = factory.getNoteDAO();
  }

  public static ImportantState createEnum(String state) {
    ImportantState result = ImportantState.JUST_REMEMBER;
    try {
      result = ImportantState.valueOf(state.toUpperCase());
    } catch (IllegalArgumentException ex) {
      ex.printStackTrace();
    }
    return result;
  }

  public Note addNote(User user, String description, String importantState, String deadlineDate)
      throws AddObjectException, ParseException {
    DateNoteService dateNoteService = new DateNoteService();
    DateNote date = dateNoteService.addDate(deadlineDate);

    Note note = createNote(user, date, description, importantState);

    noteDAO.create(note);
    return note;
  }

  private Note createNote(User user, DateNote date, String description, String importantState)
      throws ParseException {
    ImportantState state = createEnum(importantState);
    description = NoteContentEncoder.encrypt(description);

    return Note.builder()
        .description(description)
        .importantState(state)
        .statusNote(StatusNote.TODO)
        .date(date)
        .user(user)
        .build();
  }

  public List<Note> getAll(long userId) {
    List<Note> notes = noteDAO.getAll(userId);
    return decode(notes);
  }

  public List<Note> getAll(long userId, StatusNote status) {
    List<Note> notes = noteDAO.getAll(userId, status);
    return decode(notes);
  }

  public List<Note> getAll(long userId, String type, SortOptions sortBy, String order) {
    List<Note> result;
    if (type.equals("ALL")) {
      result = noteDAO.getAll(userId, sortBy);
    } else {
      StatusNote statusNote = StatusNote.valueOf(type);
      result = noteDAO.getAll(userId, statusNote, sortBy);
    }

    if (order.equals("desc") && result != null) {
      Collections.reverse(result);
    }
    return decode(result);
  }

  private List<Note> decode(List<Note> notes) {
    return notes.stream()
        .map(
            n -> {
              String encoded = NoteContentEncoder.decrypt(n.getDescription());
              n.setDescription(encoded);
              return n;
            })
        .collect(Collectors.toList());
  }

  public void deleteNote(User user, long noteId) throws DeleteObjectException, DataAccessException {
    Note note = noteDAO.read(noteId);
    checkDataLeak(user, note);
    noteDAO.delete(noteId);

    DateNoteService service = new DateNoteService();
    service.delete(note.getDate().getId());
  }

  public void update(long dateId, String description, String importantStatus)
      throws UpdateObjectException, ParseException {
    description = NoteContentEncoder.encrypt(description);

    noteDAO.update(dateId, description, importantStatus);
  }

  public void update(User user, long noteId, NoteChangeParameter action)
      throws UpdateObjectException, DataAccessException {
    Note note = noteDAO.read(noteId);
    checkDataLeak(user, note);

    DateNoteService dateNoteService = new DateNoteService();
    dateNoteService.update(note.getDate(), action);

    if (action == NoteChangeParameter.DONE) {
      note.setStatusNote(StatusNote.MADE);
    }
    if (action == NoteChangeParameter.TODO) {
      note.setStatusNote(StatusNote.TODO);
    }
    noteDAO.update(note);
  }

  public Note read(User user, long noteId) throws DataAccessException {
    Note note = noteDAO.read(noteId);
    checkDataLeak(user, note);
    note.setDescription(NoteContentEncoder.decrypt(note.getDescription()));
    return note;
  }

  public void deleteAllUserNotes(long userId) throws DeleteObjectException {
    DateNoteService dateNoteService = new DateNoteService();

    List<Note> allNotes = getAll(userId);

    for (Note note : allNotes) {
      noteDAO.delete(note.getId());
      dateNoteService.delete(note.getDate().getId());
    }
  }

  private void checkDataLeak(User user, Note note) throws DataAccessException {
    if (isNull(user, note) || !isIdEquals(user, note)) {
      throw new DataAccessException("Id user in session and note owner id not equals");
    }
  }

  private boolean isNull(User user, Note note) {
    return Objects.isNull(user) || Objects.isNull(note);
  }

  private boolean isIdEquals(User user, Note note) {
    return Objects.equals(user.getId(), note.getUser().getId());
  }
}
