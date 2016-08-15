package ru.rik.cardsnew.service.asterisk;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.event.CdrEvent;
import org.asteriskjava.manager.event.ManagerEvent;

import ru.rik.cardsnew.domain.Card;





public class AsteriskEvents implements ManagerEventListener {
		static Logger log = Logger.getLogger(AsteriskEvents.class);
	

	private ManagerConnection managerConnection;

	public AsteriskEvents()  {
		ManagerConnectionFactory factory = new ManagerConnectionFactory("localhost",  "myasterisk", "mycode");
		this.managerConnection = factory.createManagerConnection();
	}

	public void run() throws IOException, AuthenticationFailedException, TimeoutException, InterruptedException {
		managerConnection.addEventListener(this);
		managerConnection.login();
//		managerConnection.sendAction(new CoreShowChannelsAction());	
//		managerConnection.sendAction(new SipPeersAction());

	}

	public void stop() throws IllegalStateException {
		log.info("Asterisk managerConnection is logging off ");
		System.out.println("Asterisk managerConnection is logging off ");
		managerConnection.logoff();
	}
	
	 public void onManagerEvent(ManagerEvent event) {
		if (event instanceof CdrEvent) {
			CdrEvent cdrevent = (CdrEvent) event;
			if (cdrevent.getUserField() != null) {
				log.info("AnswerTime: " + cdrevent.getAnswerTime() +
					 " AnswerTimeAsDate: " + cdrevent.getAnswerTimeAsDate() +
					 " StartTime: " + cdrevent.getStartTime() +
					 " StartTimeAsDate: " + cdrevent.getStartTimeAsDate() +
					 " Src: " + cdrevent.getSrc() +
					 " Destination: " + cdrevent.getDestination() +
					 " Disposition: " + cdrevent.getDisposition() +
					 " BillableSeconds: " + cdrevent.getBillableSeconds() +
					 " Duration: " + cdrevent.getDuration() +
					 " UserField: " + cdrevent.getUserField() + 
					 " Trunk: " + cdrevent.getTrunk() +
					 " gateip: " + cdrevent.getGateip() +
					 " Regcode: " + cdrevent.getRegcode());
//				addCdr(cdrevent);
			} 
		}	

	 }
	 
//	public void addCdr(CdrEvent ce) {
//		Map<String, Card> allcards = Group.getAllcardsByName();
//		Card card;
//		String cardname = ce.getUserField();
//		if (cardname != null && (card = allcards.get(cardname)) != null) {
//			try {
//				CdrCardEvent cdr = new CdrCardEvent(ce.getStartTime(), ce.getSrc(), ce.getDestination(), card,
//						ce.getBillableSeconds(), ce.getTrunk(), ce.getDisposition(), ce.getRegcode());
//				CardStat cardStat;
//				if ((cardStat = card.getCardStat()) == null)
//					cardStat = new CardStat(card);
//				cardStat.addEvent(cdr);
//				cardStat.calcAcd();
//				cardStat.calcAsr();
//			} catch (ParseException pe) {
//				log.error("can not create CdrEvent calldate: " + ce.getStartTime() + " cardname: " + cardname, pe);
//			}
//		}
//	}

		public static void main(String[] args) throws Exception {
			AsteriskEvents helloEvents;

			helloEvents = new AsteriskEvents();
			helloEvents.run();
			Thread.sleep(20000);
	    }
}