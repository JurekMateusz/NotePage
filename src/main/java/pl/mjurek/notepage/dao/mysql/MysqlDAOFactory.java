package pl.mjurek.notepage.dao.mysql;

import pl.mjurek.notepage.dao.DAOFactory;
import pl.mjurek.notepage.dao.mysql.datenote.DateNoteDAO;
import pl.mjurek.notepage.dao.mysql.datenote.DateNoteDAOImpl;
import pl.mjurek.notepage.dao.mysql.key.KeyActionDAO;
import pl.mjurek.notepage.dao.mysql.key.KeyActionDAOImpl;
import pl.mjurek.notepage.dao.mysql.note.NoteDAO;
import pl.mjurek.notepage.dao.mysql.note.NoteDAOImpl;
import pl.mjurek.notepage.dao.mysql.user.UserDAO;
import pl.mjurek.notepage.dao.mysql.user.UserDAOImpl;

public class MysqlDAOFactory extends DAOFactory {
  @Override
  public UserDAO getUserDAO() {
    return new UserDAOImpl();
  }

  @Override
  public NoteDAO getNoteDAO() {
    return new NoteDAOImpl();
  }

  @Override
  public DateNoteDAO getDateNoteDAO() {
    return new DateNoteDAOImpl();
  }

  @Override
  public KeyActionDAO getKeyActionDAO() {
    return new KeyActionDAOImpl();
  }
}
