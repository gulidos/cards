package ru.rik.cardsnew.db;


import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.criteria.Expression;

import ru.rik.cardsnew.domain.MyEntity;


public interface GenericRepo<T extends MyEntity, S extends MyEntity>
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
	
	public S addStateIfAbsent(T entity);
	
	public T findByName(String name);
	
	public S findStateById(long id);
	
	public S findStateByName(String name);
	
	public ConcurrentMap<Long, S> getStates();

	boolean removeStateIfExists(long id);

	EntityManager getEntityManager();
}

