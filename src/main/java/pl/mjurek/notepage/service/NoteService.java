package pl.mjurek.notepage.service;

import pl.mjurek.notepage.dao.DAOFactory;
import pl.mjurek.notepage.dao.note.NoteDAO;
import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.DateNote;
import pl.mjurek.notepage.model.Note;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.model.states.ImportantState;
import pl.mjurek.notepage.model.states.NotesControllerOptions;
import pl.mjurek.notepage.model.states.SortOptions;
import pl.mjurek.notepage.model.states.StatusNote;
import pl.mjurek.notepage.service.encoding.Encode;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class NoteService {
    public static ImportantState createEnum(String state) {
        ImportantState result = ImportantState.JUST_REMEMBER;
        try {
            result = ImportantState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Note addNote(User user, String description, String importantState, String deadlineDate) throws AddObjectException, ParseException {
        DateNoteService dateNoteService = new DateNoteService();
        DateNote date = dateNoteService.addDate(deadlineDate);

        Note note = createNote(user, date, description, importantState);

        NoteDAO noteDAO = getNoteDAO();
        noteDAO.create(note);
        return note;
    }

    private Note createNote(User user, DateNote date, String description, String importantState) throws ParseException {
        ImportantState state = createEnum(importantState);
        description = encode(description);

        return Note.builder()
                .description(description)
                .importantState(state)
                .statusNote(StatusNote.TODO)
                .date(date)
                .user(user)
                .build();
    }

    public List<Note> getAll(long userId) {
        NoteDAO noteDAO = getNoteDAO();
        List<Note> notes = noteDAO.getAll(userId);
        return decode(notes);
    }

    public List<Note> getAll(long userId, StatusNote status) {
        NoteDAO noteDAO = getNoteDAO();
        List<Note> notes = noteDAO.getAll(userId, status);
        return decode(notes);
    }

    public List<Note> getAll(long userId, String type, SortOptions sortBy, String order) {
        NoteDAO noteDAO = getNoteDAO();
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
        Encode encode = Encode.getInstance();
        return notes.stream()
                .map(n -> {
                    String encoded = encode.decode(n.getDescription());
                    n.setDescription(encoded);
                    return n;
                })
                .collect(Collectors.toList());
    }


    public void deleteNote(long noteId) throws DeleteObjectException {
        NoteDAO noteDAO = getNoteDAO();
        Note note = noteDAO.read(noteId);

        noteDAO.delete(note.getId());

        DateNoteService service = new DateNoteService();
        service.delete(note.getDate().getId());
    }

    public void update(long dateId, String description, String importantStatus) throws UpdateObjectException, ParseException {
        NoteDAO noteDAO = getNoteDAO();
        description = encode(description);

        noteDAO.update(dateId, description, importantStatus);
    }

    public void update(long noteId, NotesControllerOptions action) throws UpdateObjectException {
        NoteDAO noteDAO = getNoteDAO();
        Note note = noteDAO.read(noteId);

        DateNoteService dateNoteService = new DateNoteService();
        dateNoteService.update(note.getDate(), action);

        if (action == NotesControllerOptions.DONE) {
            note.setStatusNote(StatusNote.MADE);
        }
        if (action == NotesControllerOptions.TODO) {
            note.setStatusNote(StatusNote.TODO);
        }
        noteDAO.update(note);
    }

    public Note read(long noteId) {
        NoteDAO noteDAO = getNoteDAO();
        Note note = noteDAO.read(noteId);
        Encode encode = Encode.getInstance();
        note.setDescription(encode.decode(note.getDescription()));
        return note;
    }

    public void deleteAllUserNotes(long userId) throws DeleteObjectException {
        NoteDAO noteDAO = getNoteDAO();
        DateNoteService dateNoteService = new DateNoteService();

        List<Note> allNotes = getAll(userId);

        for (Note note : allNotes) {
            noteDAO.delete(note.getId());
            dateNoteService.delete(note.getDate().getId());
        }
    }

    private NoteDAO getNoteDAO() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        return factory.getNoteDAO();
    }

    private String encode(String text) {
        Encode encode = Encode.getInstance();
        return encode.encode(text);
    }
}
