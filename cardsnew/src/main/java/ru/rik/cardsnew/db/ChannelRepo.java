package ru.rik.cardsnew.db;

import java.util.ConcurrentModificationException;
import java.util.List;

import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Trunk;

public interface ChannelRepo extends GenericRepo<Channel, ChannelState>{
	void init();
	Channel findPair(Channel ch);

	List<Channel> getSorted(Trunk t);

	List<Channel> findGroupChans(Grp grp);

	List<Channel> findBoxChans(Box box);

	/** Transactional.
	 * Fixes changing card in channel into database
	 * @throws ConcurrentModificationException 
	 */
	void switchCard(Channel chan, Card c);

}