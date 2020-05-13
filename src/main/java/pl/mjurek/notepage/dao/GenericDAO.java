package pl.mjurek.notepage.dao;

import pl.mjurek.notepage.exception.CantAddObjectException;

import java.io.Serializable;

public interface GenericDAO<T, PK extends Serializable> {
    T create(T newObject) throws CantAddObjectException;

    T read(PK primaryKey);

    T update(T updateObject);

    void delete(PK key);
}
