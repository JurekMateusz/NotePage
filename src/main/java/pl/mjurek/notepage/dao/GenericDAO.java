package pl.mjurek.notepage.dao;

import java.io.Serializable;

public interface GenericDAO<T, PK extends Serializable> {
    T create(T newObject);

    T read(PK primaryKey);

    T update(T updateObject);

    void delete(PK key);
}
