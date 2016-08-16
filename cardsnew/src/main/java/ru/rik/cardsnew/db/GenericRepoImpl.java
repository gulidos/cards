package ru.rik.cardsnew.db;

import static javax.persistence.LockModeType.NONE;
import static javax.persistence.LockModeType.OPTIMISTIC;
import static javax.persistence.LockModeType.OPTIMISTIC_FORCE_INCREMENT;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Line;
import ru.rik.cardsnew.service.asterisk.AsteriskEvents;

public abstract class GenericRepoImpl<T, ID extends Serializable> implements GenericRepo<T , ID> {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext protected EntityManager em;
	static Logger logger = Logger.getLogger(GenericRepo.class);

	
    protected final Class<T> entityClass;
    protected CriteriaBuilder cb;
    protected CriteriaQuery<T> criteria;
    protected Root<T> c;
   
    protected GenericRepoImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
      
    }
    
    @PostConstruct
    protected void Init() {
    	  this.cb = em.getCriteriaBuilder();
          this.criteria = cb.createQuery(entityClass);
          this.c = criteria.from(entityClass);
    }

    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void joinTransaction() {
        if (!em.isJoinedToTransaction())
            em.joinTransaction();
    }

    @Override
    public T findById(ID id) {
        return findById(id, NONE);
    }
    
    public T findByName(String name) {
    	TypedQuery<T> query = em
				.createQuery(criteria.select(c)
						.where(cb.equal(c.<String> get("name"), name)
				)).setHint("org.hibernate.cacheable", true);
    	
    	
    	T result = null;
    	try {
    		result = query.getSingleResult();
		} catch (NoResultException e) {
			
		} catch (Exception e1) {
			logger.error(e1.getMessage(), e1);
		}
    	
    	
		return result;
    }

    
    @Override
    public T findById(ID id, LockModeType lockModeType) {
        return em.find(entityClass, id, lockModeType);
    }

    
    @Override
    public T findReferenceById(ID id) {
        return em.getReference(entityClass, id);
    }

	@Override
	public List<T> findAll() {
		TypedQuery<T> query = em
				.createQuery(criteria.select(c))
				.setHint("org.hibernate.cacheable", true);

		return query.getResultList();
	}
	
	@Override
	public List<T> findAllRestr(Expression<Boolean> restriction) {
		TypedQuery<T> query = em
				.createQuery(criteria.select(c).where(restriction))
				.setHint("org.hibernate.cacheable", true);

		return query.getResultList();
		
    }

    @Override
    public Long getCount() {
        CriteriaQuery<Long> c = em.getCriteriaBuilder().createQuery(Long.class);
       
        c.select(em.getCriteriaBuilder().count(c.from(entityClass)));
        return em.createQuery(c).getSingleResult();
    }

    @Override
    public void checkVersion(T entity, boolean forceUpdate) {
        em.lock(entity, forceUpdate ? OPTIMISTIC_FORCE_INCREMENT : OPTIMISTIC);
    }
    
	
    @Override
    public T makePersistent(T instance) {
        // merge() handles transient AND detached instances
        return em.merge(instance);
    }

    @Override
    public void makeTransient(T instance) {
        em.remove(instance);
    }
    
    @Override
	public void clearCache() {
		Cache cache = em.getEntityManagerFactory().getCache();

		cache.evictAll();
	}

}
