package ru.rik.cardsnew.db;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.CardStat;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Trunk;

@Repository
public class ChannelRepoImpl extends GenericRepoImpl<Channel, ChannelState> implements ChannelRepo {
	private static final long serialVersionUID = 1L;
	static final Logger logger = LoggerFactory.getLogger(ChannelRepoImpl.class);
	private static ChannelRepoImpl repo;
	@Autowired private CardRepo cards;
	
	public ChannelRepoImpl() {
		super(Channel.class, ChannelState.class);
	}
	@PostConstruct
	protected void init() {
		logger.debug("initializing static variable");
		repo = this;
	}

	public static ChannelRepoImpl get() {return repo;	}
	
	
	public Channel findPair(Channel ch) {
		return em.createNamedQuery("findPair", Channel.class)
		.setParameter("box", ch.getBox())
		.setParameter("line", ch.getLine().getPair())
		.setHint("org.hibernate.cacheable", true)
		.getSingleResult();
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
		return em.createNamedQuery("findByGrp", Channel.class)
				.setParameter("grp", grp)
				.setHint("org.hibernate.cacheable", true)
				.getResultList();
	}
    	
	
	public List<Channel> findBoxChans(Box box) {
		return em.createNamedQuery("findByBox", Channel.class)
				.setParameter("box", box)
				.setHint("org.hibernate.cacheable", true)
				.getResultList();
	}	

	@Transactional
	public void switchCard(Channel chan, Card c) {
		Assert.notNull(chan);
		
		Channel persChan = findById(chan.getId());
		if (persChan.getVersion() != chan.getVersion())
			throw new ConcurrentModificationException("Channel " + persChan.getName() + " was modified");	
		
		Card oldCard = persChan.getCard();
		if (oldCard != null) {
			oldCard.setChannelId(0); // set to null channel Id
			CardStat st = oldCard.getStat();
			st.setFree(false, true);
		}	
		
		if (c != null) {
			Card newCard = cards.findById(c.getId());
			if (newCard.getVersion() != c.getVersion())
				throw new ConcurrentModificationException("Card " + newCard.getName() + " was modified");	

			newCard.setChannelId(persChan.getId());
			persChan.setCard(newCard);
		} else 
			persChan.setCard(c);

		makePersistent(persChan);
	}
}
