package ru.rik.cardsnew.db;

import static javax.persistence.LockModeType.NONE;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.Line;
import ru.rik.cardsnew.domain.repo.ChannelsStates;

@Repository
public class ChannelRepoImpl extends GenericRepoImpl<Channel, Long> {
	private static final long serialVersionUID = 1L;
	@Autowired
	private ChannelsStates channelsStates;
	
	public ChannelRepoImpl() {
		super(Channel.class);	
	}
	
	@Override
	public List<Channel> findAll() {
		List<Channel> channels = super.findAll();
		for (Channel ch : channels) {
			if (channelsStates.findById(ch.getId()) == null)
				channelsStates.add(new ChannelState(ch));
		}

		return channels;
	}
	
	 @Override
	    public Channel findById(Long id) {
		 Channel ch = findById(id, NONE);
		 if (channelsStates.findById(ch.getId()) == null)
				channelsStates.add(new ChannelState(ch));
	        return ch;
	    }
	 
	 public Channel findPair(Channel ch) {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Channel> criteria = cb.createQuery(entityClass);
			
			Root<Channel> c = criteria.from(entityClass);
			TypedQuery<Channel> query = em.createQuery(
					criteria.select(c).where(
						cb.and(
							cb.equal(c.<Line>get("line"), ch.getLine().getPair()),
							cb.equal(c.<Box>get("box"), ch.getBox())
						)	
					)
			).setHint("org.hibernate.cacheable", true);
			return query.getSingleResult();		 
	 }
}
