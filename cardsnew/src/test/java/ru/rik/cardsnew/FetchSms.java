package ru.rik.cardsnew;


import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.config.RootConfig;
import ru.rik.cardsnew.db.BankRepo;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.db.GroupRepo;
import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.ChannelState.Status;
import ru.rik.cardsnew.domain.Place;
import ru.rik.cardsnew.domain.State;
import ru.rik.cardsnew.service.TaskCompleter;
import ru.rik.cardsnew.service.TaskDescr;
import ru.rik.cardsnew.service.telnet.SmsTask;
import ru.rik.cardsnew.service.telnet.TelnetHelper;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = ConfigJpaH2.class)
@ContextConfiguration(classes = RootConfig.class)

public class FetchSms {
	@Autowired private TelnetHelper h;
	@Autowired private ChannelRepo chans;
	@Autowired private CardRepo cards;
	@Autowired private TaskCompleter taskCompleter;
	@Autowired private GroupRepo groups;
	@Autowired private BankRepo banks;
	
	
	public FetchSms() {	}
	
//	@Test
	public void fakTest()  {
		
	}
//	@Test
	@Rollback(false)
	public void installRealCards() throws InterruptedException {
		chans.setCardToNull(chans.findAll());
		cards.setChannelToNull(cards.findAll());
		
//		Set<Channel> simSetJobs = new HashSet<>();
//		
//		for (Channel ch : chans.findAll()) {
//			ChannelState st = ch.getState(chans);
//			if (simSetJobs.contains(ch)) {
//				simSetJobs.remove(ch);
//			} else {
//				Channel pair = ch.getPair(chans);
//				if (pair != null)
//					simSetJobs.add(pair);
//				taskCompleter.addTask(() -> SimSet.get(ch, pair), st);
//			}
//		}
		
		Map<Future<State>, TaskDescr> map  = taskCompleter.getMap();
		for (int i = 0; i < 30; i++) {
			Thread.sleep(1000);
			System.out.println("size: " + map.size());
			for (Future<State> s: map.keySet()) {
				System.out.println(map.get(s).getName() + " " + map.get(s).getClazz());
			}	

		}
		chans.findAll().stream().map(ch -> ch.getState(chans))
			.peek(st -> System.out.print("channel: " + st.getName()+ " "))
			.filter(st -> st.getSimset() != null)
			.map(st -> st.getSimset())
			.forEach(ss -> {
				Place place = Place.getInstance(ss.getCardPos());
				Bank bank = banks.findByName(ss.getBankIp()) ;
				Card c = cards.findCardsByPlace(place, bank);
				try {
					chans.switchCard(ss.getCh(), c);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

	}
	
	@Test
	@Transactional
	public void getSms() throws InterruptedException, ExecutionException {
		Set<Channel> telnetJobs = new HashSet<>();
		groups.findById(7).getChannels().stream()
//		chans.findAll().stream()
//			.filter(ch -> (ch.getCard() != null))
			.peek(ch -> System.out.println(ch.getName() + " pair: " + ch.getPair(chans).getName()))
			.filter(ch-> ch.getCard() != null)
			.forEach(ch -> { 
					if (telnetJobs.contains(ch))
						telnetJobs.remove(ch);
					else {
						ChannelState st = ch.getState(chans);
						Channel peer = ch.getPair(chans);
						telnetJobs.add(peer);
						telnetJobs.add(ch);
						st.setStatus(Status.Ready);
						st.setStatus(Status.Smsfetch);
						TaskDescr td = new TaskDescr(SmsTask.class, st, new Date());
						taskCompleter.addTask(() -> SmsTask.get(h, ch, ch.getCard(), peer, peer.getCard(), td),	td);
					}
			}); 
		
		
		Map<Future<State>, TaskDescr> map  = taskCompleter.getMap();
		for (int i = 0; i < 100; i++) {
			Thread.sleep(500);
			System.out.println("");
			System.out.println("========================================== size: " + map.size());
			for (Future<State> s: map.keySet()) {
				TaskDescr descr = map.get(s);
				System.out.println(descr.getName() + " " + descr.getClazz().getSimpleName() + " " 
				+ (new Date().getTime() - descr.getLastChangeDate().getTime())/1000 +
						" " + descr.getStage() );

			}	

		}
		

	}
	
}
