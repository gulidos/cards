package ru.rik.cardsnew.domain.repo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;



public abstract class GenericMemImpl<T> implements GenericMem<T> {

	protected final Class<T> eClass; //entity Class
	private ConcurrentMap<String, T> emap;
	
	public GenericMemImpl(Class<T> entityClass) {
		 this.eClass = entityClass;
		 this.emap = new ConcurrentHashMap<>();
	}

	@Override
	public T add(String key, T e) {
		return emap.putIfAbsent(key, e);
	}

}
