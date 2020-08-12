package pl.mjurek.notepage.dao.mysql.datenote;

import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.datenote.DateNote;

import java.sql.Timestamp;

public interface DateNoteDAO {
  DateNote create(DateNote dateNote) throws AddObjectException;

  DateNote read(Long dateId);

  DateNote update(DateNote dateNote) throws UpdateObjectException;

  void delete(Long key) throws DeleteObjectException;

  void update(long dateId, Timestamp deadline) throws UpdateObjectException;
}
