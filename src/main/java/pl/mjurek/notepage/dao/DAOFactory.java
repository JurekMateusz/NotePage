package pl.mjurek.notepage.dao;

import pl.mjurek.notepage.dao.mysql.MysqlDAOFactory;
import pl.mjurek.notepage.dao.mysql.datenote.DateNoteDAO;
import pl.mjurek.notepage.dao.mysql.key.KeyActionDAO;
import pl.mjurek.notepage.dao.mysql.note.NoteDAO;
import pl.mjurek.notepage.dao.mysql.user.UserDAO;
import pl.mjurek.notepage.exception.NoSuchDbTypeException;

public abstract class DAOFactory {
  public static final int MYSQL_DAO_FACTORY = 1;

  public static DAOFactory getDAOFactory() {
    DAOFactory factory = null;
    try {
      factory = getDAOFactory(MYSQL_DAO_FACTORY);
    } catch (NoSuchDbTypeException e) {
      e.printStackTrace();
    }
    return factory;
  }

  public static DAOFactory getDAOFactory(int type) throws NoSuchDbTypeException {
    switch (type) {
      case MYSQL_DAO_FACTORY:
        return new MysqlDAOFactory();
      default:
        throw new NoSuchDbTypeException("DataBase type don't exist.Type: " + type);
    }
  }

  public abstract UserDAO getUserDAO();

  public abstract NoteDAO getNoteDAO();

  public abstract DateNoteDAO getDateNoteDAO();

  public abstract KeyActionDAO getKeyActionDAO();
}
