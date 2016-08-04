package ru.rik.cardsnew.db;


import java.io.Serializable;
import java.util.List;

import javax.persistence.LockModeType;


public interface GenericRepo<T, ID extends Serializable>
    extends Serializable {

    void joinTransaction();

    T findById(ID id);

    T findById(ID id, LockModeType lockModeType);

    T findReferenceById(ID id);

    List<T> findAll();

    Long getCount();

    T makePersistent(T entity);

    void makeTransient(T entity);

    void checkVersion(T entity, boolean forceUpdate);

	void clearCache();
}

