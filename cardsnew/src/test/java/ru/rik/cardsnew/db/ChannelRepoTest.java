package ru.rik.cardsnew.db;

import java.text.ParseException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.ConfigJpaH2;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.ChannelState.Status;
import ru.rik.cardsnew.domain.Oper;
import ru.rik.cardsnew.domain.Route;
import ru.rik.cardsnew.domain.Trunk;
import ru.rik.cardsnew.domain.Util;
import ru.rik.cardsnew.domain.events.Cdr;
import ru.rik.cardsnew.domain.repo.Cdrs;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigJpaH2.class)
public class ChannelRepoTest {
	@Autowired private  CardRepo cards;
	@Autowired private  ChannelRepo chans;
	@Autowired private Cdrs cdrs;
	@Autowired private  TrunkRepo trunks;
	
	private Trunk t;

	private Route route;
	public ChannelRepoTest() {	}
	
	@Before
	public void initBefore() {
		
	}
	
	
	@Test @Transactional
	public void insertCard() {
		long cardId = 1l;
		long channelId = 1l;
		
		Card c = cards.findById(cardId);
		Channel ch  = chans.findById(channelId);
		Assert.assertNotNull(c);
		Assert.assertNotNull(ch);
		Assert.assertNull(ch.getCard());
		Assert.assertEquals(c.getChannelId(), 0);
		
		// switching

		chans.findAll().stream()
		.forEach((Channel cha) -> System.out.println("!!=== " + cha.getId() + " " + cha.getName()));
//		+ " state: " + cha.getState(chans) != null ?  cha.getState(chans).getId() : 999));
		Assert.assertNotNull(ch.getState(chans));
		chans.switchCard(ch, c);
		
		Assert.assertEquals(ch.getCard(), c);
		Assert.assertEquals(c.getChannelId(), ch.getId());
	}
	
	
	@Test @Transactional
	public void allActiveAndRotating() {
		activate5channels();
		List<Channel> list = chans.getSorted(t, route);
		System.out.println("=============================");
		for(Channel ch: list) System.out.println(ch.toString() + ch.getState(chans).toString());
		Assert.assertTrue(list.size() == 5); 

		Assert.assertTrue(list.get(0).getId() == 1);
		list = chans.getSorted(t, route);
		Assert.assertTrue(list.get(0).getId() == 2);
		list = chans.getSorted(t, route);
		Assert.assertTrue(list.get(0).getId() == 3);
		list = chans.getSorted(t, route);
		Assert.assertTrue(list.get(0).getId() == 4);
		list = chans.getSorted(t, route);
		Assert.assertTrue(list.get(0).getId() == 5);
	}

	
	@Test @Transactional
	public void notActiveNotRotating() {
		activate5channels();
		List<Channel> list = chans.getSorted(t,  route);
		list.get(0).setEnabled(false);
		list = chans.getSorted(t,  route);
		Assert.assertTrue(list.size() == 4);
	}
	
	@Test @Transactional
	public void setAllNotActiveReturnEmpty() {
		activate5channels();
		
		List<Channel> list = chans.getSorted(t,  route);
		for (Channel ch : list) ch.setEnabled(false);
		
		list = chans.getSorted(t,  route);
		Assert.assertTrue(list.size() == 0);
	}
	
	
	@Test @Transactional
	public void ifStatIdNotReadyNotTake() {
		activate5channels();
		chans.findStateById(1).setStatus(Status.Failed);
		chans.findStateById(2).setStatus(Status.Inchange);
		chans.findStateById(3).setStatus(Status.Unreach);
		List<Channel> list = chans.getSorted(t,  route);
		Assert.assertTrue(list.size() == 2);
	}
	
	@Test @Transactional
	public void ifOverallRemainTimeOfCardInChanIsOverSkip() throws ParseException {
		activate5channels();
		route = Route.builder().oper(Oper.GREEN).regcode(1).build();

		List<Channel> list = chans.getSorted(t,  route);
		System.out.println(list.size());
		Assert.assertTrue(list.size() == 5);
		
		Card c1 = chans.findById(1).getCard();
		int billsec = c1.getDlimit() * 60 + 1;
		
		Cdr cdr = Cdr.builder().date(Util.getNowMinusSec(1200)).src("11112").dst("22222").cardId(1).billsec(billsec).trunk("trnk1")
				.disp("ANSWERED").regcode("77").uniqueid("1234567892").channelId(1).build();
		
		c1.getStat(cards).applyCdr(cdr, c1, chans.findById(1), cdrs);
		list = chans.getSorted(t,  route);
		Assert.assertTrue(list.size() == 4);
	}
	
	@Test @Transactional
	public void whenDeleteChannelAssureStatAlsoDeleted() {
		Channel ch = chans.findById(1);
		Assert.assertNotNull(ch);
		chans.makeTransient(ch);
		
		ChannelState cs = chans.findStateById(ch.getId());
		Assert.assertNull(cs);
		
	}
	
	@Test @Transactional
	public void changeChannelNameChechStatesNameAlsoChanged() {
		Channel ch = chans.findById(1);
		Assert.assertNotNull(ch);
		String name = "new name";
		ch.setName(name);
		chans.makePersistent(ch);
		
		Assert.assertEquals(ch.getState(chans).getName(), name);
	}
	
	@Transactional
	private void activate5channels() {
		route = Route.builder().oper(Oper.GREEN).regcode(77).build();
		t = trunks.findById(1);
		t.getChannels().forEach(ch -> ch.hashCode()); // reinit (reread) each of the channel
		
		for (int i = 0; i <= chans.getCount(); i++) 
			chans.removeStateIfExists(i);
		chans.init();
		
		for (int i = 0; i <= cards.getCount(); i++) 
			cards.removeStateIfExists(i);
		cards.init();
		
		Channel ch1 = chans.findById(1); 
		Assert.assertNotNull(ch1);
		chans.switchCard(ch1, cards.findById(1));
		ch1.getState(chans).setStatus(Status.Ready);
		
		Channel ch2 = chans.findById(2);
		Assert.assertNotNull(ch2);
		chans.switchCard(ch2, cards.findById(2));
		ch2.getState(chans).setStatus(Status.Ready);
		
		Channel ch3 = chans.findById(3);
		Assert.assertNotNull(ch3);
		chans.switchCard(ch3, cards.findById(3));
		ch3.getState(chans).setStatus(Status.Ready);
		
		Channel ch4 = chans.findById(4);
		Assert.assertNotNull(ch4);
		chans.switchCard(ch4, cards.findById(4));
		ch4.getState(chans).setStatus(Status.Ready);
		
		Channel ch5 = chans.findById(5);
		Assert.assertNotNull(ch5);
		chans.switchCard(ch5, cards.findById(5));
		ch5.getState(chans).setStatus(Status.Ready);
		
		Assert.assertNotNull(ch1.getCard());
	}


	
}
