package pl.mjurek.notepage.service;

import pl.mjurek.notepage.dao.DAOFactory;
import pl.mjurek.notepage.dao.datenote.DateNoteDAO;
import pl.mjurek.notepage.dao.note.NoteDAO;
import pl.mjurek.notepage.model.DateNote;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateNoteService {
    public DateNote addDate(String deadlineDate) throws ParseException {
        DateNote date = createDate(deadlineDate);
        DateNoteDAO noteDAO = getDateNoteDAO();
        DateNote result = noteDAO.create(date);
        return result;
    }

    private DateNote createDate(String deadlineDate) throws ParseException {
        Date date = new SimpleDateFormat("yyyy/MM/dd").parse(deadlineDate);
        Timestamp deadlineTimestamp = new Timestamp(date.getTime());
        DateNote result = DateNote.builder()
                .dateStickNote(new Timestamp(new Date().getTime()))
                .dateDeadlineNote(deadlineTimestamp)
                .build();

        return result;
    }

    private DateNoteDAO getDateNoteDAO() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        return factory.getDateNoteDAO();
    }
}
