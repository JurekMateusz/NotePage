package pl.mjurek.notepage.dao;

import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;

import java.io.Serializable;

public interface GenericDAO<T, PK extends Serializable> {
    T create(T newObject) throws AddObjectException;

    T read(PK primaryKey);

    T update(T updateObject);

    void delete(PK key) throws DeleteObjectException;
}
