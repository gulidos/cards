package ru.rik.cardsnew.domain.repo;

import java.util.concurrent.ConcurrentMap;

public interface GenericMem<T> {
	
	public T add(String key, T entity);
	
	public ConcurrentMap<String, T> getMap( );
	
}
