package ru.rik.cardsnew.db;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.Line;
import ru.rik.cardsnew.domain.Trunk;

@Repository
public class ChannelRepoImpl extends GenericRepoImpl<Channel, ChannelState> {
	private static final long serialVersionUID = 1L;
	static final Logger logger = LoggerFactory.getLogger(ChannelRepoImpl.class);

	public ChannelRepoImpl() {
		super(Channel.class, ChannelState.class);
	}

	
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
	
	
}
