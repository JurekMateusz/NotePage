package pl.mjurek.notepage.service;

import pl.mjurek.notepage.dao.DAOFactory;
import pl.mjurek.notepage.dao.note.NoteDAO;
import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.*;


import java.text.ParseException;
import java.util.List;


public class NoteService {
    public Note addNote(User user, String description, String importantState, String deadlineDate) throws AddObjectException, ParseException {
        DateNoteService noteService = new DateNoteService();
        DateNote date = noteService.addDate(deadlineDate);

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

    private ImportantState createEnum(String state) {
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


    public void deleteNote(long noteId) throws DeleteObjectException {
        NoteDAO noteDAO = getNoteDAO();
        Note note = noteDAO.read(noteId);

        noteDAO.delete(note.getId());

        DateNoteService service = new DateNoteService();
        service.delete(note.getDate().getId());
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
        return noteDAO.read(noteId);
    }

    private NoteDAO getNoteDAO() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        return factory.getNoteDAO();
    }


}
