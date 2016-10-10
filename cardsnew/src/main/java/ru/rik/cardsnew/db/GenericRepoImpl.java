package ru.rik.cardsnew.db;

import static javax.persistence.LockModeType.NONE;
import static javax.persistence.LockModeType.OPTIMISTIC;
import static javax.persistence.LockModeType.OPTIMISTIC_FORCE_INCREMENT;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;
import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import ru.rik.cardsnew.domain.MyEntity;
import ru.rik.cardsnew.domain.State;

public abstract class GenericRepoImpl<T extends MyEntity, S extends State> implements GenericRepo<T, S> {
	static final Logger logger = LoggerFactory.getLogger(GenericRepoImpl.class);
	private static final long serialVersionUID = 1L;

	@PersistenceContext
	protected EntityManager em;

	protected final Class<T> entityClass;
	protected final Class<S> entityStateClass;
	protected CriteriaBuilder cb;
	protected final ConcurrentMap<Long, S> statsById;
	protected final ConcurrentMap<String, S> statsByName;

	protected GenericRepoImpl(Class<T> entityClass, Class<S> entityStateClass) {
		this.entityClass = entityClass;
		this.entityStateClass = entityStateClass;
		statsById = new ConcurrentHashMap<>();
		statsByName = new ConcurrentHashMap<>();
	}

	@PostConstruct
	protected void Init() {
		logger.debug("post constructor initialisation {} repo", entityClass.getName());
		this.cb = em.getCriteriaBuilder();

		for (T entity : findAll())
			addStateIfAbsent(entity);
	}

	@Override
	public T findById(long id) {
		return findById(id, NONE);
	}

	@Override
	public T findById(long id, LockModeType lockModeType) {
		return em.find(entityClass, id, lockModeType);
	}

	// public T findByName(String name) {
	// CriteriaQuery<T> criteria = cb.createQuery(entityClass);
	// Root<T> c = criteria.from(entityClass);
	// TypedQuery<T> query = em
	// .createQuery(criteria.select(c)
	// .where(cb.equal(c.<String> get("name"), name)
	// )).setHint("org.hibernate.cacheable", true);
	// T result = null;
	// try {
	// result = query.getSingleResult();
	// } catch (NoResultException e) {
	//
	// } catch (Exception e1) {
	// logger.error(e1.getMessage(), e1);
	// }
	//
	// return result;
	// }

	public T findByName(String name) {
		Assert.notNull(name);
		S state = findStateByName(name);

		if (state == null)
			return null;
		long id = state.getId();

		return findById(id);
	}

	@Override
	public List<T> findAll() {
		CriteriaQuery<T> criteria = cb.createQuery(entityClass);
		Root<T> c = criteria.from(entityClass);
		TypedQuery<T> query = em.createQuery(criteria.select(c)).setHint("org.hibernate.cacheable", true);

		return query.getResultList();
	}

	@Override
	public List<T> findAllRestr(Expression<Boolean> restriction) {
		CriteriaQuery<T> criteria = cb.createQuery(entityClass);
		Root<T> c = criteria.from(entityClass);
		TypedQuery<T> query = em.createQuery(criteria.select(c).where(restriction)).setHint("org.hibernate.cacheable",
				true);

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
		T newInst = em.merge(instance);
		addStateIfAbsent(newInst);
		return newInst;
	}

	@Override
	public void makeTransient(T instance) {
		em.remove(instance);

		S s = findStateById(instance.getId());
		if (s != null)
			removeStateIfExists(s);
	}

	@Override
	public void clearCache() {
		Cache cache = em.getEntityManagerFactory().getCache();
		cache.evictAll();
	}

	public EntityManager getEntityManager() {
		return em;
	}

	// ============== State methods =====================

	public S addStateIfAbsent(T entity) {
		if (entity == null)
			throw new IllegalArgumentException("entity can not be null here");
		S state = null;
		long id = entity.getId();
		if (findStateById(id) == null) {
			try {
				S newState = entityStateClass.newInstance();
				newState.setId(id);
				newState.setName(entity.getName());
				state = statsById.putIfAbsent(id, newState);
				statsByName.putIfAbsent(entity.getName(), newState);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return state;
	}

	public boolean removeStateIfExists(S s) {
		S removed = statsById.remove(s.getId());
		statsByName.remove(s.getName());
		return removed != null;
	}

	public S findStateById(long id) {
		return statsById.get(id);
	}

	public S findStateByName(String name) {
		Assert.notNull(name);
		return statsByName.get(name);
	}

	public ConcurrentMap<Long, S> getStates() {
		return statsById;
	}
}
