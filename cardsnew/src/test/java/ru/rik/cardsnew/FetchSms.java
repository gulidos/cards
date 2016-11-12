package ru.rik.cardsnew;

import java.util.concurrent.Callable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Line;
import ru.rik.cardsnew.domain.State;
import ru.rik.cardsnew.service.TaskCompleter;
import ru.rik.cardsnew.service.telnet.SmsTask;
import ru.rik.cardsnew.service.telnet.TelnetHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigJpaH2.class)

public class FetchSms {
	@Autowired TelnetHelper h;
	@Autowired ChannelRepo repo;
	@Autowired TaskCompleter taskCompleter;
	
	public FetchSms() {	}

	@Test
	@Transactional
	public void getSms() throws InterruptedException {
//		Channel ch = repo.findById(7);
//		Channel peer = ch.getPair(repo);
		Channel pair = Channel.builder()
				.box(Box.builder().ip("192.168.200.45").build())
				.line(Line.L1)
				.build();
//		ChannelState st = repo.findStateById(ch.getId());
		

		for (Channel ch : repo.findAll()) {
			System.out.println(ch.getName());
			Callable<State> getsms = () -> SmsTask.get(h, ch, null);
			taskCompleter.addTask(getsms, ch.getState(repo));
		}
		
		Thread.sleep(120000);

	}
}
