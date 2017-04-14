package ru.rik.cardsnew.service;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.ConfigJpaH2;
import ru.rik.cardsnew.db.BalanceRepo;
import ru.rik.cardsnew.db.BankRepo;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.domain.Ussd;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.CardStat;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.ChannelState.Status;
import ru.rik.cardsnew.domain.Sms;
import ru.rik.cardsnew.domain.Util;
import ru.rik.cardsnew.service.telnet.SmsTask;
import ru.rik.cardsnew.service.telnet.SmsTask.Phase;
import ru.rik.cardsnew.service.telnet.TelnetHelper;
import ru.rik.cardsnew.service.telnet.TelnetHelperMock;
import ru.rik.cardsnew.service.telnet.UssdTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigJpaH2.class)

public class SmsAndUssdTests {
	@Autowired private  CardRepo cards;
	@Autowired private  ChannelRepo chans;
	@Autowired private  BankRepo banks; 
	@Autowired private TaskCompleter taskCompleter;
	
	private TelnetHelper th;
	private TelnetClient tc;
	private Channel ch;
	private Card card;
	private ChannelState st, pairSt;
	private Channel pair;
	private Card cardPair;
	
	public SmsAndUssdTests() {	}
	
	@Before 
	@Transactional
	public void init() throws SocketException, IOException, InterruptedException {
		tc = mock(TelnetClient.class);
		th = new TelnetHelperMock(tc);
		taskCompleter.setChans(chans);
		taskCompleter.setTelnetHandler(th);
		initChannels();
	}

	
	private void initChannels() throws InterruptedException {
		
		ch = chans.switchCard(chans.findById(1), cards.findById(1));
		st = chans.findStateById(ch.getId());
		st.setStatus(Status.Ready);
		st.setStatus(Status.Smsfetch);
		card = ch.getCard();		
	
		pair = chans.switchCard(chans.findById(146), cards.findById(2));
		pairSt = chans.findStateById(pair.getId());
		pairSt.setStatus(Status.Ready);
		pairSt.setStatus(Status.Smsfetch);
		cardPair = pair.getCard();
	}
	

//	@After
	public void destroy () {
		chans.switchCard(ch, null);
		chans.switchCard(pair, null);
	}
	
	
	@Test
	public void checkMainChannelNoSms() throws IOException, InterruptedException {
		Assert.assertNotNull(pair);
		Assert.assertNotNull(cardPair);
		Assert.assertNotNull(ch.getCard());
		Assert.assertNotNull(pair.getCard());
		Assert.assertTrue(card.getChannelId() != 0);
		Assert.assertTrue(cardPair.getChannelId() != 0);
		TaskDescr td = new TaskDescr(SmsTask.class, st, new Date());
		SmsTask task = new SmsTask(ch, card, null, null, tc, new ArrayList<Sms>(), Phase.FetchMain, td); 
		taskCompleter.handleSms(task);
		Thread.sleep(20);
		Assert.assertEquals(task.getPhase(), Phase.FetchMain);
		verify(tc, times(1)).disconnect();
		Assert.assertEquals(st.getStatus(), Status.Ready);

	}
	
	@Test(expected = IllegalArgumentException.class)
	public void checkCardIsNull() throws IOException {
		Assert.assertNotNull(ch.getCard());
		Assert.assertNotNull(pair.getCard());
		Assert.assertTrue(card.getChannelId() != 0);
		Assert.assertTrue(cardPair.getChannelId() != 0);
		TaskDescr td = new TaskDescr(SmsTask.class, st, new Date());
		SmsTask task = new SmsTask(ch, null, pair, null, tc, new ArrayList<Sms>(), Phase.FetchMain, td); 
		taskCompleter.handleSms(task);
		Assert.assertEquals(st.getStatus(), Status.Ready);
	}
	
	@Test 
	public void ifMainHasSmsDeleteThem() throws InterruptedException, IOException {
		Assert.assertNotNull(ch.getCard());
		Assert.assertNotNull(pair.getCard());
		Assert.assertTrue(card.getChannelId() != 0);
		Assert.assertTrue(cardPair.getChannelId() != 0);

		List<Sms> smslist = new ArrayList<Sms>();
		smslist.add(Sms.builder().id(1).date(new Date()).channel(ch).card(card).decodedmsg("test")
				.build());
//		smslist.add(new Sms(1, 1, "test", new Date(),  "test", card, ch));
		TaskDescr td = new TaskDescr(SmsTask.class, st, new Date());
		SmsTask task = new SmsTask(ch, card, null, null, tc, smslist, Phase.FetchMain, td); 
		taskCompleter.handleSms(task);
		Thread.sleep(200);
		Assert.assertEquals(task.getPhase(), Phase.DeleteMain);
		verify(tc, times(1)).disconnect();
		Assert.assertEquals(st.getStatus(), Status.Ready);
	}
	
	@Test 
	public void ifThereIsPairFetchFromIt() throws InterruptedException, IOException {
		Assert.assertNotNull(ch.getCard());
		Assert.assertNotNull(pair.getCard());
		Assert.assertTrue(card.getChannelId() != 0);
		Assert.assertTrue(cardPair.getChannelId() != 0);
		
		List<Sms> smslist = new ArrayList<Sms>();
//		smslist.add(new Sms(2, 2, "test", new Date(), "test", cardPair, ch));
		smslist.add(Sms.builder().id(2).date(new Date()).channel(ch).card(cardPair).decodedmsg("test")
				.build());
		((TelnetHelperMock) th).setPairSmses(smslist);
		TaskDescr td = new TaskDescr(SmsTask.class, st, new Date());
		SmsTask task = SmsTask.get(th, ch, card, pair, cardPair, td);
		
		taskCompleter.handleSms(task);
		Thread.sleep(300);
		Assert.assertEquals(task.getPhase(), Phase.DeletePair);
		verify(tc, times(1)).disconnect();
		Assert.assertEquals(st.getStatus(), Status.Ready);
	}
	
	
	// ==================== Ussd =========================
	@Test
	public void chechBalanceAndCheckCardState() throws SocketException, IOException, InterruptedException {
		TaskDescr td = new TaskDescr(UssdTask.class, st, new Date());
		Card c = cards.findById(1);
		CardStat cs = c.getStat(cards);
		float oldbalance = cs.getBalance();
		Assert.assertEquals(oldbalance, 112, 0.1);

		UssdTask task = UssdTask.get(th, ch, c, "*100#", td);
		UssdTask SpyTask = spy(task);
		Ussd b = Ussd.builder().date(new Date()).balance(105.99f)
			.card(c).decodedmsg("105.99р.\n" + "Смотрите самое интересное видео! Трафик бесплатно (8р/д)*213#")
			.payment(false)
			.build();
		doReturn(b).when(SpyTask).getUssd();
		
		st.setStatus(Status.UssdReq);
		taskCompleter.handleUssd(SpyTask);
		Thread.sleep(300);
		
		Assert.assertEquals(st.getStatus(), Status.Ready);
		Assert.assertEquals(cs.getBalance(), 105.99f, 0.1);
		Assert.assertFalse(cs.isRefilled());
		Assert.assertTrue(Util.isApproxEqual(cs.getLastBalanceChecked(), new Date(), 1000));
		cs.setBalance(oldbalance);
	}
	
	@Autowired BalanceRepo balances;
	@Test
	public void lastBalanceInTableIsPayment() throws SocketException, IOException, InterruptedException {
		TaskDescr td = new TaskDescr(UssdTask.class, st, new Date());
		Card c = cards.findById(4);
		CardStat cs = c.getStat(cards);
		Assert.assertFalse(c.isEligibleToInstall(cards, banks));
		Assert.assertNotNull(ch);
		Assert.assertNotNull(st);
		Assert.assertNotNull(pair);
		Assert.assertNotNull(pairSt);
		UssdTask SpyTask = spy(UssdTask.get(th, ch, c, "*100#", td));
		Ussd b = Ussd.builder().date(new Date()).balance(0.99f)
				.card(c).decodedmsg("0.99р. ").payment(true).build();
		doReturn(b).when(SpyTask).getUssd();

		st.setStatus(Status.UssdReq);		
		
		taskCompleter.handleUssd(SpyTask);
		Thread.sleep(300);
		
		Assert.assertEquals(cs.getBalance(), 0, 0.1);
		Assert.assertTrue(cs.isRefilled());
		Assert.assertTrue(c.isEligibleToInstall(cards, banks));
	}
	
	@Test
	public void getUssdAndNeedSmsIsTrue() throws SocketException, IOException, InterruptedException {
		UssdTask SpyTask = spy(UssdTask.get(th, ch, card, "*100#", new TaskDescr(UssdTask.class, st, new Date())));
		Ussd b = Ussd.builder().date(new Date())
				.card(card).decodedmsg("Спасибо за обращение! Мы направим ответ на Ваш запрос в SMS")
				.payment(false).smsNeeded(true)
				.build();
		doReturn(b).when(SpyTask).getUssd();
		
		st.setStatus(Status.UssdReq);
		taskCompleter.handleUssd(SpyTask);
		Thread.sleep(300);
		Assert.assertEquals(st.getStatus(), Status.Smsfetch);
		Assert.assertTrue(Util.isApproxEqual(st.getNextSmsFetchDate(), Util.getNowPlusSec(60), 2000));
	}
	
}
