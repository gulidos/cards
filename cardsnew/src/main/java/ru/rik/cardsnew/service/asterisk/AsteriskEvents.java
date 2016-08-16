package ru.rik.cardsnew.service.asterisk;

import java.io.IOException;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.event.CdrEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.springframework.beans.factory.annotation.Autowired;

import ru.rik.cardsnew.db.CardRepoImpl;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.CardStat;
import ru.rik.cardsnew.domain.events.CdrCardEvent;
import ru.rik.cardsnew.domain.repo.CardsStates;

public class AsteriskEvents implements ManagerEventListener {
	static Logger log = Logger.getLogger(AsteriskEvents.class);

	private ManagerConnection managerConnection;
	
	@Autowired private CardRepoImpl cardRepo;
	@Autowired private CardsStates cardsStates;

	public AsteriskEvents() {
		ManagerConnectionFactory factory = new ManagerConnectionFactory("localhost", "myasterisk", "mycode");
		this.managerConnection = factory.createManagerConnection();
	}

	public void start() throws IOException, AuthenticationFailedException, TimeoutException, InterruptedException {
		managerConnection.addEventListener(this);
		managerConnection.login();
	}

	public void stop() throws IllegalStateException {
		log.info("Asterisk managerConnection is logging off ");
		managerConnection.logoff();
	}

	public void onManagerEvent(ManagerEvent event) {
		if (event instanceof CdrEvent) {
			CdrEvent cdrevent = (CdrEvent) event;
			if (cdrevent.getUserField() != null) {
				log.debug("AnswerTime: " + cdrevent.getAnswerTime() + " AnswerTimeAsDate: "
						+ cdrevent.getAnswerTimeAsDate() + " StartTime: " + cdrevent.getStartTime()
						+ " StartTimeAsDate: " + cdrevent.getStartTimeAsDate() + " Src: " + cdrevent.getSrc()
						+ " Destination: " + cdrevent.getDestination() + " Disposition: " + cdrevent.getDisposition()
						+ " BillableSeconds: " + cdrevent.getBillableSeconds() + " Duration: " + cdrevent.getDuration()
						+ " UserField: " + cdrevent.getUserField() + " Trunk: " + cdrevent.getTrunk() + " gateip: "
						+ cdrevent.getGateip() + " Regcode: " + cdrevent.getRegcode());
				addCdr(cdrevent);
			}
		}

	}

	public void addCdr(CdrEvent ce) {
		String cardname = ce.getUserField();
		if (cardname == null ) return;
		
		Card card = cardRepo.findByName(cardname);
		if (card != null) {
			try {
				CdrCardEvent cdr = new CdrCardEvent(ce.getStartTime(), ce.getSrc(), ce.getDestination(), card.getId(),
						ce.getBillableSeconds(), ce.getTrunk(), ce.getDisposition(), ce.getRegcode());
				
				CardStat cardStat = cardsStates.findById(card.getId());
				if (cardStat == null) {
					cardStat = new CardStat(card);
					cardsStates.add(cardStat);
				}	
				cardStat.addEvent(cdr);
				cardStat.calcAcd();
				cardStat.calcAsr();
			} catch (ParseException pe) {
				log.error("can not create CdrEvent calldate: " + ce.getStartTime() + " cardname: " + cardname, pe);
			}
		} else 
			log.debug("The card with name " + cardname + " does not exist");
	}

}