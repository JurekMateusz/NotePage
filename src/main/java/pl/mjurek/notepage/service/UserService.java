package pl.mjurek.notepage.service;

import org.apache.commons.codec.digest.DigestUtils;

import pl.mjurek.notepage.dao.DAOFactory;
import pl.mjurek.notepage.dao.user.UserDAO;
import pl.mjurek.notepage.model.User;

public class UserService {
    public User addUser(User user) {
        UserDAO userDAO = getUserDAO();
        User result = userDAO.create(user);
        return result;
    }

    private void codeUserPassword(User user) {

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

    private UserDAO getUserDAO() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        return factory.getUserDAO();
    }
}