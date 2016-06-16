package ru.rik.cardsnew.domain.repo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ru.rik.cardsnew.db.GenericRepo;
import ru.rik.cardsnew.domain.MyEntity;



public abstract class GenericMemImpl<T extends MyEntity> implements GenericMem<T> {

	protected final Class<T> eClass; //entity Class
	private ConcurrentMap<Long, T> emap;
	private  GenericRepo<T, Long> repo;
	
	public GenericMemImpl(Class<T> entityClass, GenericRepo<T, Long> repo) {
		 this.eClass = entityClass;
		 this.emap = new ConcurrentHashMap<>();
		 this.repo = repo;
	}

	@Override
	public T add(Long key, T e) {
		return emap.putIfAbsent(key, e);
	}
	
	@Override
	public ConcurrentMap<Long, T> getMap( ) {
		return emap;
	}
	
	@Override
	public T findById(Long id) {
		return emap.get(id);
	}
	
	@Override
	public T findByName(String name) {
		if (name == null ) 
			throw new NullPointerException("Id can not be null!");
		for (T e: emap.values()) {
			if (e.getName() == name)
				return e;
		}
		return null;
	}
	
	@Override
	public T update(T e) {
//		if (emap.get(e.getName()) == null)
//			throw new IllegalArgumentException("The entity doesn't exist");
		return repo.makePersistent(e);
	}
	
	@Override
	public void clear() {
		emap.clear();
	}
}
