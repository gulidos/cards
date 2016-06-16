package ru.rik.cardsnew.domain.repo;

import java.util.concurrent.ConcurrentMap;

public interface GenericMem<T> {
	
	public T add(Long key, T entity);
	
	public ConcurrentMap<Long, T> getMap( );

	public T findByName(String name);

	public T update(T entity);

	T findById(Long id);

	void clear();
	
}
