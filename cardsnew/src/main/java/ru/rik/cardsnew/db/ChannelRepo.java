package ru.rik.cardsnew.db;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Line;
import ru.rik.cardsnew.domain.Trunk;

@Repository
public class ChannelRepo extends GenericRepoImpl<Channel, ChannelState> {
	private static final long serialVersionUID = 1L;
	static final Logger logger = LoggerFactory.getLogger(ChannelRepo.class);
	private static ChannelRepo repo;

	public ChannelRepo() {
		super(Channel.class, ChannelState.class);
	}
	@PostConstruct
	protected void init() {
		logger.debug("initializing static variable");
		repo = this;
	}

	public static ChannelRepo get() {return repo;	}
	
	public Channel findPair(Channel ch) {
		CriteriaQuery<Channel> criteria = cb.createQuery(entityClass);
    	Root<Channel> c = criteria.from(entityClass);
		TypedQuery<Channel> query = em
				.createQuery(
						criteria.select(c)
								.where(cb.and(cb.equal(c.<Line> get("line"), ch.getLine().getPair()),
										cb.equal(c.<Box> get("box"), ch.getBox()))))
				.setHint("org.hibernate.cacheable", true);
		return query.getSingleResult();
	}
	
	
	public List<Channel> getSorted(Trunk t) throws NullPointerException {
		if (t == null)	throw new NullPointerException("trunk can not be null");
		
		List<Channel> result = t.getChannels().stream()
				.filter(ch -> ch.getCard() != null && ch.isEnabled())
				.sorted((ch1, ch2) -> Integer.compare(ch1.getState().getPriority(), 
						ch2.getState().getPriority()))
				.collect(Collectors.toList());
		if (result.size() > 0)
			result.get(0).getState().incPriority();
		return result;
	}
	
	
	public List<Channel> findGroupChans(Grp grp) {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Channel> criteria = cb.createQuery(Channel.class);

    	Root<Channel> root = criteria.from(Channel.class);
		TypedQuery<Channel> query = em
				.createQuery(
						criteria.select(root).where(cb.equal(root.get("group"), cb.parameter(Grp.class, "group"))))
				.setParameter("group", grp)
				.setHint("org.hibernate.cacheable", true);

		return query.getResultList();
    }
	
	public List<Channel> findBoxChans(Box box) {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Channel> criteria = cb.createQuery(Channel.class);

    	Root<Channel> root = criteria.from(Channel.class);
		TypedQuery<Channel> query = em
				.createQuery(
						criteria.select(root).where(cb.equal(root.get("box"), cb.parameter(Box.class, "box"))))
				.setParameter("box", box)
				.setHint("org.hibernate.cacheable", true);

		return query.getResultList();
    }
}
