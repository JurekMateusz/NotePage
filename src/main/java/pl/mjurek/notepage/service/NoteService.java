package pl.mjurek.notepage.service;

import pl.mjurek.notepage.dao.DAOFactory;
import pl.mjurek.notepage.dao.note.NoteDAO;
import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.*;


import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class NoteService {
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

        Note result = Note.builder()
                .description(description)
                .importantState(state)
                .statusNote(StatusNote.TODO)
                .date(date)
                .user(user)
                .build();

        return result;
    }

    public static ImportantState createEnum(String state) {
        ImportantState result = ImportantState.JUST_REMEMBER;
        try {
            result = ImportantState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException ex) {

        }
        return result;
    }

    public List<Note> getAll(long userId) {
        NoteDAO noteDAO = getNoteDAO();
        return noteDAO.getAll(userId);
    }

    public List<Note> getAll(long userId, StatusNote status) {
        NoteDAO noteDAO = getNoteDAO();
        return noteDAO.getAll(userId, status);
    }

    public List<Note> getAll(long userId, String type, String orderByColumn, String order) {
        NoteDAO noteDAO = getNoteDAO();
        List<Note> result;
        if (type.equals("ALL")) {
            result = noteDAO.getAll(userId, orderByColumn);
        } else {
            StatusNote statusNote = StatusNote.valueOf(type);
            result = noteDAO.getAll(userId, statusNote, orderByColumn);
        }

        if (order.equals("desc")) {
            Collections.reverse(result);
        }
        return result;
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
        noteDAO.update(dateId, description, importantStatus);
    }

    public void update(long noteId, NotesControllerOptions action) throws UpdateObjectException {
        NoteDAO noteDAO = getNoteDAO();
        Note note = noteDAO.read(noteId);//todo to many quarry

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
        return noteDAO.read(noteId);
    }

    private NoteDAO getNoteDAO() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        return factory.getNoteDAO();
    }
}
