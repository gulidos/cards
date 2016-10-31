package ru.rik.cardsnew.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.Setter;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState.Status;
import ru.rik.cardsnew.domain.State;
import ru.rik.cardsnew.service.http.SimSet;

public class Switcher implements State{
	private static final Logger logger = LoggerFactory.getLogger(Switcher.class);		
	@Autowired ChannelRepo chans;
	@Setter @Getter private long id;
	@Setter @Getter private String name;
	@Setter @Getter private long cardId;
	@Setter @Getter private String cardName;
	
	public Switcher() {	}
	
	public Switcher(long id, String name, long cardId, String cardName) {
		super();
		this.id = id;
		this.name = name;
		this.cardId = cardId;
		this.cardName = cardName;
	}

	
	public State switchCard (Channel ch, Card c) {
		Channel pair = ch.getPair();
		try {
			if (c!= null)
				c.engage();
			logger.debug("card {} engaged for channel {}", c.getName(), ch.getName());
			SimSet.get(ch, null);
			logger.debug("channel {} available", ch.getName());

			try {
				chans.switchCard(ch, c);
				logger.debug("write to db", c.getName(), ch.getName());
			} catch (Exception e) {
				logger.error(e.toString(), e);
				if (c!= null)
					c.getStat().setFree(false, true);
			}
			try { 
				if (pair != null)
					while (pair.getState().isInUse()) {
						logger.debug("awaiting for peer chanel {}",  pair.getName());
						ch.getState().setStatus(Status.AwaitForPeer);
						TimeUnit.SECONDS.sleep(10);
					}
				SimSet.post(ch, c); 
				logger.debug("switching chanel {}",  ch.getName());
			} catch (Exception e) {
				logger.error(e.toString(), e);
				if (c!= null)
					c.getStat().setFree(false, true);
				// TODO bring back the old card in place
				throw new RuntimeException(e.getMessage(), e);
			}
			ch.getState().setStatus(Status.Inchange);
			if (pair != null)
				pair.getState().setStatus(Status.PeerInchange);	
			logger.debug("changing chanel status on Inchange");
		} catch (Exception e) {
			logger.error(e.toString(), e);		
			throw new RuntimeException(e.getMessage(), e);
		}
		return new Switcher(ch.getId(), ch.getName(), c.getId(), c.getName());
	}



	@Override
	public Class<?> getClazz() {
		return Switcher.class;
	}


	

}
