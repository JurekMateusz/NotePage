package pl.mjurek.notepage.service;

import pl.mjurek.notepage.dao.DAOFactory;
import pl.mjurek.notepage.dao.datenote.DateNoteDAO;
import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.model.DateNote;
import pl.mjurek.notepage.model.NotesControllerOptions;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateNoteService {
    public DateNote addDate(String deadlineDate) throws ParseException, AddObjectException {
        DateNote date = createDate(deadlineDate);
        DateNoteDAO noteDAO = getDateNoteDAO();
        DateNote result = noteDAO.create(date);
        return result;
    }

    private DateNote createDate(String deadlineDate) throws ParseException {
        Date date = new SimpleDateFormat("MM/dd/yyyy").parse(deadlineDate);
        Timestamp deadlineTimestamp = new Timestamp(date.getTime());
        DateNote result = DateNote.builder()
                .dateStickNote(new Timestamp(new Date().getTime()))
                .dateDeadlineNote(deadlineTimestamp)
                .build();

        return result;
    }
    public void update(DateNote dateNote,NotesControllerOptions action){
        if(action == NotesControllerOptions.DONE){
            dateNote.setDateUserMadeTask(new Timestamp(new Date().getTime()));
        }
        if(action == NotesControllerOptions.TODO){
           dateNote.setDateUserMadeTask(null);
        }
        DateNoteDAO dateNoteDAO = getDateNoteDAO();
        dateNoteDAO.update(dateNote);
    }

    public void delete(long dateId) throws DeleteObjectException {
        DateNoteDAO noteDAO = getDateNoteDAO();
        noteDAO.delete(dateId);
    }

    private DateNoteDAO getDateNoteDAO() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        return factory.getDateNoteDAO();
    }
}
