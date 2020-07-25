package pl.mjurek.notepage.dao;

import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;

import java.io.Serializable;

// Fajnie że bawisz się generykami ale to jest zbędne, jak już to lepiej podzielić na kilka interfejsów (bo nie każde dao może robić update - i co wtedy?
// Metodę i tak musisz zaimplementować
public interface GenericDAO<T, PK extends Serializable> {
    T create(T newObject) throws AddObjectException;

    T read(PK primaryKey);

    T update(T updateObject) throws UpdateObjectException;

    void delete(PK key) throws DeleteObjectException;
}
