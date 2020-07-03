package pl.mjurek.notepage.service;

import pl.mjurek.notepage.dao.DAOFactory;
import pl.mjurek.notepage.dao.user.UserDAO;
import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.User;

public class UserService {
    public User addUser(User user) throws AddObjectException {
        UserDAO userDAO = getUserDAO();
        User result = userDAO.create(user);
        return result;
    }

    public User getUserByUserName(String name) {
        UserDAO userDAO = getUserDAO();
        User result = userDAO.getUserByUserName(name);
        return result;
    }

    public User update(User user) throws UpdateObjectException {
        UserDAO userDAO = getUserDAO();
        User result = userDAO.update(user);
        return result;
    }

    public void delete(User user) {
        UserDAO userDAO = getUserDAO();
        long userId = user.getId();
        try {
            userDAO.updateVerification(userId, "NO");  //here i assure user can't log to account before delete all data
        } catch (UpdateObjectException e) {
            e.printStackTrace();
        }

        sleep(); // to go out from this thread and send user response.

        NoteService noteService = new NoteService();
        try {
            noteService.deleteAllUserNotes(userId);
            userDAO.delete(userId);
        } catch (DeleteObjectException e) {
            e.printStackTrace();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isNameExisting(String name) {
        UserDAO userDAO = getUserDAO();
        User user = userDAO.getUserByUserName(name);
        if (user == null) {
            return false;
        }
        return true;
    }

    public boolean isEmailExisting(String email) {
        UserDAO userDAO = getUserDAO();
        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            return false;
        }
        return true;
    }

    public void unblock(long userId) throws UpdateObjectException {
        UserDAO userDAO = getUserDAO();
        userDAO.updateVerification(userId, "YES");
    }

    private UserDAO getUserDAO() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        return factory.getUserDAO();
    }
}
