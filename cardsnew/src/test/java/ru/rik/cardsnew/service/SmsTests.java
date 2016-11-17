package ru.rik.cardsnew.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;

import org.apache.commons.net.telnet.TelnetClient;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.ConfigJpaH2;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.ChannelState.Status;
import ru.rik.cardsnew.domain.Sms;
import ru.rik.cardsnew.domain.State;
import ru.rik.cardsnew.service.telnet.SmsTask;
import ru.rik.cardsnew.service.telnet.SmsTask.Phase;
import ru.rik.cardsnew.service.telnet.TelnetHelper;

//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigJpaH2.class)
public class SmsTests {
	@Autowired private  CardRepo cards;
	@Autowired private  ChannelRepo chans;
	private CompletionService<State> completionService;
	private ThreadPoolTaskExecutor taskExecutor;
	private TelnetHelper th;
	private TelnetClient tc;
	private TaskCompleter taskCompleter;
	private Channel ch;
	Card card;
	ChannelState st;
	Channel pair;
	
	public SmsTests() {	}
	
//	@Before
	public void init() throws SocketException, IOException {
		completionService = mock(CompletionService.class);
		when(completionService.submit(any())).thenReturn(mock(Future.class));
		taskExecutor = mock(ThreadPoolTaskExecutor.class);
		th = mock(TelnetHelper.class);
		tc = mock(TelnetClient.class);
		taskCompleter = new TaskCompleter( completionService, taskExecutor );
		taskCompleter.setChans(chans);
		when(th.getConnection(any(), anyInt(), any(), any())).thenReturn(tc);
		ch = chans.findById(1);
		card = cards.findById(1);
		chans.switchCard(ch, card);
		st = chans.findStateById(ch.getId());
		pair = ch.getPair(chans);
		st.setStatus(Status.Ready);
		st.setStatus(Status.Smsfetch);
	}
	
//	@Test @Transactional
	public void checkMainChannelNoSms() throws IOException {
		SmsTask task = new SmsTask(ch, card, pair, null, tc, new ArrayList<Sms>(), Phase.FetchMain); 
		taskCompleter.handleSms(task);
		Assert.assertEquals(task.getPhase(), Phase.FetchMain);
		verify(tc, times(1)).disconnect();
	}
	
//	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void checkCardIsNull() throws IOException {
		SmsTask task = new SmsTask(ch, null, pair, null, tc, new ArrayList<Sms>(), Phase.FetchMain); 
		taskCompleter.handleSms(task);
	}
	
//	@Test @Transactional
	public void ifMainHasSmsDeleteThem() throws InterruptedException {
		List<Sms> smslist = new ArrayList<Sms>();
		smslist.add(new Sms(1, 1, "test", new Date(), "test", "test", card, ch));
		SmsTask task = new SmsTask(ch, card, pair, null, tc, smslist, Phase.FetchMain); 
		taskCompleter.handleSms(task);
		
//		Assert.assertEquals(task.getPhase(), Phase.DeleteMain);
//		verify(th, times(1)).deleteSms(tc, smslist);
	}
	
}
