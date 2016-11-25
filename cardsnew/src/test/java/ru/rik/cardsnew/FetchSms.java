package ru.rik.cardsnew;


import java.io.IOException;
import java.net.SocketException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.config.RootConfig;
import ru.rik.cardsnew.db.BankRepo;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.db.GroupRepo;
import ru.rik.cardsnew.db.JpaConfig;
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
import ru.rik.cardsnew.service.telnet.TelnetHelperImpl;



public class FetchSms {
	public FetchSms() {	}

	public static void main(String[] args) throws SocketException, IOException, InterruptedException {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(RootConfig.class);
		ChannelRepo chans= ctx.getBean(ChannelRepo.class);
		TaskCompleter taskCompleter = ctx.getBean(TaskCompleter.class);
		TelnetHelper h = ctx.getBean(TelnetHelperImpl.class);
		Set<Channel> telnetJobs = new HashSet<>();
		chans.findAll().stream()
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
