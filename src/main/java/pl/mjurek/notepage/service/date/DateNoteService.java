package pl.mjurek.notepage.service.date;

import pl.mjurek.notepage.dao.DAOFactory;
import pl.mjurek.notepage.dao.mysql.datenote.DateNoteDAO;
import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.datenote.DateNote;
import pl.mjurek.notepage.service.note.NoteChangeParameter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateNoteService {
  private final DateNoteDAO dateNoteDAO;

  public DateNoteService() {
    DAOFactory factory = DAOFactory.getDAOFactory();
    dateNoteDAO = factory.getDateNoteDAO();
  }

  public DateNote addDate(String deadlineDate) throws ParseException, AddObjectException {
    DateNote date = createDate(deadlineDate);
    return dateNoteDAO.create(date);
  }

  private DateNote createDate(String deadlineDate) throws ParseException {
    Date date = new SimpleDateFormat("MM/dd/yyyy").parse(deadlineDate);
    Timestamp deadlineTimestamp = new Timestamp(date.getTime());

    return DateNote.builder()
        .dateStickNote(new Timestamp(new Date().getTime()))
        .dateDeadlineNote(deadlineTimestamp)
        .build();
  }

  public void update(DateNote dateNote, NoteChangeParameter action) throws UpdateObjectException {
    if (action == NoteChangeParameter.DONE) {
      dateNote.setDateUserMadeTask(new Timestamp(new Date().getTime()));
    }
    if (action == NoteChangeParameter.TODO) {
      dateNote.setDateUserMadeTask(null);
    }
    dateNoteDAO.update(dateNote);
  }

  public void update(long dateId, String deadline) throws UpdateObjectException, ParseException {
    Date date = new SimpleDateFormat("MM/dd/yyyy").parse(deadline);
    Timestamp deadlineTimestamp = new Timestamp(date.getTime());

    dateNoteDAO.update(dateId, deadlineTimestamp);
  }

  public void delete(long dateId) throws DeleteObjectException {
    dateNoteDAO.delete(dateId);
  }
}
