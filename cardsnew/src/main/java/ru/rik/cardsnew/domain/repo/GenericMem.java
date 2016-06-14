package ru.rik.cardsnew.domain.repo;

public interface GenericMem<T> {
	
	public T add(String key, T entity);
	
}
