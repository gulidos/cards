package ru.rik.cardsnew;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.config.RootConfig;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState.Status;
import ru.rik.cardsnew.service.TaskCompleter;
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
	
	public FetchSms() {	}

	@Test
	@Transactional

	public void getSms() throws InterruptedException {
//		activate5channels();
//		Channel ch = repo.findById(7);
//		Channel peer = ch.getPair(repo);
//		Channel pair = Channel.builder()
//				.box(Box.builder().ip("192.168.200.45").build())
//				.line(Line.L1)
//				.build();
//		ChannelState st = repo.findStateById(ch.getId());
		
		chans.findAll().stream()
//			.filter(ch -> (ch.getCard() != null))
			.peek(ch -> System.out.println(ch.getName() + " pair: " + ch.getPair(chans).getName()))
			.forEach(ch -> taskCompleter.addTask(
					() -> SmsTask.get(h, ch, null, ch.getPair(chans), null), 
					ch.getState(chans)));
			
		
//		for (Channel ch : chans.findAll()) {
//			System.out.println(ch.getName());
//			Callable<State> getsms = () -> SmsTask.get(h, ch, ch.getCard(), null, null);
//			taskCompleter.addTask(getsms, ch.getState(chans));
//		}
		
		Thread.sleep(600000);

	}
	
	
	@Test
	@Transactional
	public void saveSms() {

	}
	
	
	private void activate5channels() {
//		route = Route.builder().oper(Oper.GREEN).regcode(77).build();
//		t = trunks.findById(1);
		
		for (int i = 1; i < 6; i++) 
			chans.removeStateIfExists(i);
		chans.init();
		
		for (int i = 1; i < 16; i++) 
			cards.removeStateIfExists(i);
		cards.init();
		
		Channel ch1 = chans.findById(1); 
		chans.switchCard(ch1, cards.findById(1));
		ch1.getState(chans).setStatus(Status.Ready);
		
		Channel ch2 = chans.findById(2); 
		chans.switchCard(ch2, cards.findById(2));
		ch2.getState(chans).setStatus(Status.Ready);
		
		Channel ch3 = chans.findById(3); 
		chans.switchCard(ch3, cards.findById(3));
		ch3.getState(chans).setStatus(Status.Ready);
		
		Channel ch4 = chans.findById(4); 
		chans.switchCard(ch4, cards.findById(4));
		ch4.getState(chans).setStatus(Status.Ready);
		
		Channel ch5 = chans.findById(5); 
		chans.switchCard(ch5, cards.findById(5));
		ch5.getState(chans).setStatus(Status.Ready);
	}
	
}
