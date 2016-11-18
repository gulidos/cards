package ru.rik.cardsnew.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.telnet.TelnetClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.ConfigJpaH2;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.ChannelState.Status;
import ru.rik.cardsnew.domain.Sms;
import ru.rik.cardsnew.service.telnet.SmsTask;
import ru.rik.cardsnew.service.telnet.SmsTask.Phase;
import ru.rik.cardsnew.service.telnet.TelnetHelper;
import ru.rik.cardsnew.service.telnet.TelnetHelperMock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigJpaH2.class)
public class SmsTests {
	@Autowired private  CardRepo cards;
	@Autowired private  ChannelRepo chans;
	@Autowired private TaskCompleter taskCompleter;
	
	private TelnetHelper th;
	private TelnetClient tc;
	private Channel ch;
	Card card;
	ChannelState st;
	Channel pair;
	Card cardPair;
	
	public SmsTests() {	}
	
	@Before 
	@Transactional
	@Rollback(false) 
	public void init() throws SocketException, IOException {
		tc = mock(TelnetClient.class);
//		th = mock(TelnetHelper.class);
		th = new TelnetHelperMock(tc);
		
		taskCompleter.setChans(chans);
		taskCompleter.setTelnetHandler(th);
//		when(th.getConnection(any(), anyInt(), any(), any())).thenReturn(tc);
		ch = chans.findById(1);
		card = cards.findById(1);
		chans.switchCard(ch, card);
		st = chans.findStateById(ch.getId());
		pair = chans.findById(2);
		cardPair = cards.findById(2);
		chans.switchCard(pair, cardPair);
		st.setStatus(Status.Ready);
		st.setStatus(Status.Smsfetch);
	}
	
//	
//	@After
//	public void destroy () {
//		chans.switchCard(ch, null);
//		chans.switchCard(pair, null);
//		
//	}
	
	
	@Test
	public void checkMainChannelNoSms() throws IOException, InterruptedException {
		SmsTask task = new SmsTask(ch, card, null, null, tc, new ArrayList<Sms>(), Phase.FetchMain); 
		taskCompleter.handleSms(task);
		Thread.sleep(20);
		Assert.assertEquals(task.getPhase(), Phase.FetchMain);
		verify(tc, times(1)).disconnect();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void checkCardIsNull() throws IOException {
		SmsTask task = new SmsTask(ch, null, pair, null, tc, new ArrayList<Sms>(), Phase.FetchMain); 
		taskCompleter.handleSms(task);
	}
	
	@Test 
	public void ifMainHasSmsDeleteThem() throws InterruptedException, IOException {
		System.out.println("telnet mock: " + tc);
		List<Sms> smslist = new ArrayList<Sms>();
		smslist.add(new Sms(1, 1, "test", new Date(), "test", "test", card, ch));
		SmsTask task = new SmsTask(ch, card, null, null, tc, smslist, Phase.FetchMain); 
		taskCompleter.handleSms(task);
		Thread.sleep(200);
		Assert.assertEquals(task.getPhase(), Phase.DeleteMain);
		verify(tc, times(1)).disconnect();
	}
	
	@Test 
	public void ifThereIsPairFetchFromIt() throws InterruptedException, IOException {
		Assert.assertNotNull(ch);
		Assert.assertNotNull(card);
		Assert.assertNotNull(pair);
		Assert.assertNotNull(cardPair);
		Assert.assertNotNull(ch.getCard());
		Assert.assertNotNull(pair.getCard());
		Assert.assertTrue(card.getChannelId() != 0);
		Assert.assertTrue(cardPair.getChannelId() != 0);
		
//		SmsTask task = new SmsTask(ch, card, pair, cardPair, tc, new ArrayList<Sms>(), Phase.FetchMain);
		List<Sms> smslist = new ArrayList<Sms>();
		smslist.add(new Sms(2, 2, "test", new Date(), "test", "test", cardPair, ch));
		((TelnetHelperMock) th).setPairSmses(smslist);
		SmsTask task = SmsTask.get(th, ch, card, pair, cardPair);
		
		taskCompleter.handleSms(task);
		
		
		Thread.sleep(300);
		Assert.assertEquals(task.getPhase(), Phase.DeletePair);
		verify(tc, times(1)).disconnect();
		

	}
}
