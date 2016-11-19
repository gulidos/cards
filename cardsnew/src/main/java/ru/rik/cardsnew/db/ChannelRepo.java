package ru.rik.cardsnew.db;

import java.util.ConcurrentModificationException;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Route;
import ru.rik.cardsnew.domain.Sms;
import ru.rik.cardsnew.domain.Trunk;

public interface ChannelRepo extends GenericRepo<Channel, ChannelState>{
	void init();
	Channel findPair(Channel ch);

	List<Channel> findGroupChans(Grp grp);

	List<Channel> findBoxChans(Box box);

	/** Transactional.
	 * Fixes changing card in channel into database
	 * @return 
	 * @throws ConcurrentModificationException 
	 */
	Channel switchCard(Channel chan, Card c);

	List<Channel> getSorted(Trunk t, Route route);
	
	@Transactional void smsSave(List<Sms> list);
	void setCardToNull(List<Channel> list);

}