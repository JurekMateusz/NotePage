package pl.mjurek.notepage.dao.user;

import pl.mjurek.notepage.model.User;

public class UserDAOImpl implements UserDAO {
    @Override
    public User getUserByUserName(String userName) {
        return null;
    }

    @Override
    public boolean isNotExit(String userName) {
        return false;
    }

    @Override
    public User create(User newObject) {
        return null;
    }

    @Override
    public User read(Long primaryKey) {
        return null;
    }

    @Override
    public User update(User updateObject) {
        return null;
    }

    @Override
    public void delete(Long key) {

    }
}
