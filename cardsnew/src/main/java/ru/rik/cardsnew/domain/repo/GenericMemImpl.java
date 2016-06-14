package ru.rik.cardsnew.domain.repo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ru.rik.cardsnew.db.GenericRepo;
import ru.rik.cardsnew.domain.MyEntity;



public abstract class GenericMemImpl<T extends MyEntity> implements GenericMem<T> {

	protected final Class<T> eClass; //entity Class
	private ConcurrentMap<String, T> emap;
	private  GenericRepo<T, Long> repo;
	
	public GenericMemImpl(Class<T> entityClass, GenericRepo<T, Long> repo) {
		 this.eClass = entityClass;
		 this.emap = new ConcurrentHashMap<>();
		 this.repo = repo;
	}

	@Override
	public T add(String key, T e) {
		return emap.putIfAbsent(key, e);
	}
	
	@Override
	public ConcurrentMap<String, T> getMap( ) {
		return emap;
	}
	
	@Override
	public T findByName(String name) {
		return emap.get(name);
	}
	
	@Override
	public T findById(Long id) {
		if (id == null ) 
			throw new NullPointerException("Id can not be null!");
		for (T e: emap.values()) {
			if (e.getId() == id)
				return e;
		}
		return null;
	}
	
	@Override
	public T update(T e) {
		if (emap.get(e.getName()) == null)
			throw new IllegalArgumentException("The entity doesn't exist");
		return repo.makePersistent(e);
	}

	
}
