package ru.rik.cardsnew.db;


import java.io.Serializable;
import java.util.List;

import javax.persistence.LockModeType;
import javax.persistence.criteria.Expression;

import ru.rik.cardsnew.domain.State;


public interface GenericRepo<T extends State, S extends State>
    extends Serializable {

//    void joinTransaction();

    T findById(long id);

    T findById(long id, LockModeType lockModeType);

//    T findReferenceById(ID id);

    List<T> findAll();

    List<T> findAllRestr(Expression<Boolean> restriction);
    
    Long getCount();

    T makePersistent(T entity);

    void makeTransient(T entity);

    void checkVersion(T entity, boolean forceUpdate);

	void clearCache();
}

