package ru.rik.cardsnew;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.config.RootConfig;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.db.GroupRepo;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.ChannelState.Status;
import ru.rik.cardsnew.domain.State;
import ru.rik.cardsnew.service.TaskCompleter;
import ru.rik.cardsnew.service.telnet.SmsTask;
import ru.rik.cardsnew.service.telnet.TelnetHelper;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = ConfigJpaH2.class)
@ContextConfiguration(classes = RootConfig.class)

public class FetchSms {
	@Autowired private TelnetHelper h;
	@Autowired private ChannelRepo chans;
	@Autowired private CardRepo cards;
	@Autowired private TaskCompleter taskCompleter;
	@Autowired private GroupRepo groups;
	
	
	public FetchSms() {	}

//	@Test
	@Transactional

	public void getSms() throws InterruptedException, ExecutionException {
		Set<Channel> telnetJobs = new HashSet<>();
		groups.findById(7).getChannels().stream()
//		chans.findAll().stream()
//			.filter(ch -> (ch.getCard() != null))
			.peek(ch -> System.out.println(ch.getName() + " pair: " + ch.getPair(chans).getName()))
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
						taskCompleter.addTask(() -> SmsTask.get(h, ch, null, peer, null), st);
					}
			}); 
		
		
		Map<Future<State>, State> map  = taskCompleter.getMap();
		for (int i = 0; i < 100; i++) {
			Thread.sleep(3000);
			System.out.println("size: " + map.size());
			for (Future<State> s: map.keySet()) {

				if (map.get(s).getClass() == ChannelState.class) {
					ChannelState st = (ChannelState) map.get(s);
					if (st.getStatus() == Status.Smsfetch)
						System.out.println(st.getName() + " " + st.getStatus());
				}
			}	

		}
		

	}
	
}
