package ru.rik.cardsnew.service.asterisk;

import java.io.IOException;
import java.text.ParseException;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.event.CdrEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ru.rik.cardsnew.db.CardRepoImpl;
import ru.rik.cardsnew.db.GenericRepoImpl;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.CardStat;
import ru.rik.cardsnew.domain.events.CdrCardEvent;
import ru.rik.cardsnew.domain.repo.CardsStates;

public class AsteriskEvents implements ManagerEventListener {
	static final Logger logger = LoggerFactory.getLogger(GenericRepoImpl.class);

	private ManagerConnection managerConnection;
	
	@Autowired private CardRepoImpl cardRepo;
	@Autowired private CardsStates cardsStates;

	public AsteriskEvents() {
		ManagerConnectionFactory factory = new ManagerConnectionFactory("localhost", "myasterisk", "mycode");
		this.managerConnection = factory.createManagerConnection();
	}

	public void start() throws IOException, AuthenticationFailedException, TimeoutException, InterruptedException {
		logger.info("Asterisk managerConnection log in ");

		managerConnection.addEventListener(this);
		managerConnection.login();
	}

	public void stop() throws IllegalStateException {
		logger.info("Asterisk managerConnection is logging off ");
		managerConnection.logoff();
	}

	public void onManagerEvent(ManagerEvent event) {
		if (event instanceof CdrEvent) {
			CdrEvent cdrevent = (CdrEvent) event;
			if (cdrevent.getUserField() != null) {
				logger.debug("AnswerTime: " + cdrevent.getAnswerTime() + " AnswerTimeAsDate: "
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
				CdrCardEvent cdr = CdrCardEvent.builder()
						.date(ce.getStartTime())
						.src(ce.getSrc())
						.dst(ce.getDestination())
						.cardId(card.getId())
						.billsec(ce.getBillableSeconds())
						.trunk(ce.getTrunk())
						.disp(ce.getDisposition())
						.regcode(ce.getRegcode())
						.build();
				
				CardStat cardStat = cardsStates.findById(card.getId());
				if (cardStat == null) {
					cardStat = new CardStat(card);
					cardsStates.add(cardStat);
				}	
				cardStat.addEvent(cdr);
				cardStat.calcAcd();
				cardStat.calcAsr();
			} catch (ParseException pe) {
				logger.error("can not create CdrEvent calldate: " + ce.getStartTime() + " cardname: " + cardname, pe);
			}
		} else 
			logger.debug("The card with name " + cardname + " does not exist");
	}

}