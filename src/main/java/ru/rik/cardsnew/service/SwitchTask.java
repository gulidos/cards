package ru.rik.cardsnew.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.Setter;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState.Status;
import ru.rik.cardsnew.domain.State;
import ru.rik.cardsnew.service.asterisk.AsteriskEvents;
import ru.rik.cardsnew.service.http.SimSet;

public class SwitchTask implements State{
	private static final Logger logger = LoggerFactory.getLogger(SwitchTask.class);		
	@Autowired ChannelRepo chans;
	@Autowired CardRepo cards;
	@Autowired private AsteriskEvents astMngr;
	@Setter @Getter private long id;
	@Setter @Getter private String name;
	@Setter @Getter private long cardId;
	@Setter @Getter private String cardName;
	@Setter @Getter private TaskDescr td;
	
	
	public SwitchTask() {	}
	
	public SwitchTask(long id, String name, long cardId, String cardName, TaskDescr td) {
		super();
		this.id = id;
		this.name = name;
		this.cardId = cardId;
		this.cardName = cardName;
		this.td = td;
	}

	
	public SwitchTask switchCard (Channel ch, Card c, TaskDescr td) {
		td.setStage("in " + ch.getName() +  " installing card " + c.getName() + " write to Db");
		Channel pair = ch.getPair(chans);
		try {
			if (c!= null)
				c.engage(cards.findStateById(c.getId()));
			logger.debug("card {} engaged for channel {}",  c!=null ? c.getName() : "-", ch.getName());
			SimSet.get(ch, null, td);
			logger.debug("channel {} available", ch.getName());

			try {
				chans.switchCard(ch, c);
				logger.debug("write to db",  c!=null ? c.getName() : "-", ch.getName());
			} catch (Exception e) {
				logger.error(e.toString(), e);
				if (c!= null)
					c.getStat(cards).setFree(false, true);
			}
			try { 
				if (pair != null)
					while (pair.getState(chans).isInUse(astMngr)) {
						td.setStage("in " + ch.getName() +  " installing card " 
									+ c.getName() + " awaiting for peer " + pair.getName());
						logger.debug("awaiting for peer chanel {}",  pair.getName());
						ch.getState(chans).setStatus(Status.AwaitForPeer);
						TimeUnit.SECONDS.sleep(10);
					}
				SimSet.post(ch, c); 
				logger.debug("switching chanel {}",  ch.getName());
			} catch (Exception e) {
				logger.error(e.toString(), e);
				if (c!= null)
					c.getStat(cards).setFree(false, true);
				// TODO bring back the old card in place
				throw new RuntimeException(e.getMessage(), e);
			}
			ch.getState(chans).setStatus(Status.Inchange);
			if (pair != null)
				pair.getState(chans).setStatus(Status.PeerInchange);	
			logger.debug("changing chanel status on Inchange");
		} catch (Exception e) {
			logger.error(e.toString(), e);		
			throw new RuntimeException(e.getMessage(), e);
		}
		return new SwitchTask(ch.getId(), ch.getName(), 
				c!= null ? c.getId() : 0, 
						c!= null ?  c.getName() : "-", td);
	}


	@Override
	public Class<?> getClazz() {
		return SwitchTask.class;
	}


	

}
