package pl.mjurek.notepage.service;

import pl.mjurek.notepage.dao.DAOFactory;
import pl.mjurek.notepage.dao.note.NoteDAO;
import pl.mjurek.notepage.exception.CantAddObjectException;
import pl.mjurek.notepage.model.DateNote;
import pl.mjurek.notepage.model.ImportantState;
import pl.mjurek.notepage.model.Note;
import pl.mjurek.notepage.model.User;


import java.text.ParseException;


public class NoteService {
    public Note addNote(User user, String description, String importantState, String deadlineDate) throws CantAddObjectException, ParseException {
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

    private NoteDAO getNoteDAO() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        return factory.getNoteDAO();
    }
}
