package ru.rik.cardsnew.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ru.rik.cardsnew.db.BankRepo;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.BankState;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.ChannelState.Status;
import ru.rik.cardsnew.domain.State;
import ru.rik.cardsnew.service.http.BankStatus;
import ru.rik.cardsnew.service.http.GsmState;
import ru.rik.cardsnew.service.http.HttpHelper;
import ru.rik.cardsnew.service.http.SimSet;
import ru.rik.cardsnew.service.telnet.SmsTask;
import ru.rik.cardsnew.service.telnet.TelnetHelper;
@Service
public class PeriodicTasks {
	private static final Logger logger = LoggerFactory.getLogger(PeriodicTasks.class);		
	
	@Autowired AsyncTasks asyncTasks;
	@Autowired ChannelRepo chans;
	@Autowired CardRepo cards;
	@Autowired BankRepo bankRepo;
	@Autowired HttpHelper httpHelper;
	@Autowired TelnetHelper telnetHelper;
	@Autowired TaskCompleter taskCompleter;

	public PeriodicTasks() {
		logger.info("Instantiate the PeriodicTasks ...");

	}
	
	@Scheduled(fixedRate = 15000)
	public void checkChannels() {
		Set<Channel> pairsJobs = new HashSet<>();
		
		for (Channel ch : chans.findAll()) { 
			if (!ch.isEnabled()) continue;
			
			ChannelState st = chans.findStateById(ch.getId());
			if (!st.isGsmDateFresh()) 
				taskCompleter.addTask(()-> GsmState.get(ch),
						new TaskDescriptor(GsmState.class, st, new Date()));

			if (pairsJobs.contains(ch)) { // if the channel was already requested as a pair
				pairsJobs.remove(ch);
			} else {
				Channel pair = ch.getPair(chans);
				ChannelState pairSt = chans.findStateById(pair.getId());
				if (pair != null)
					pairsJobs.add(pair);
				if (!st.isSimSetDateFresh()) 
					taskCompleter.addTask(()-> SimSet.get(ch, pair), new TaskDescriptor(SimSet.class, st, new Date()));
				if (!st.isSmsFetchDateFresh()) {
					st.setStatus(Status.Smsfetch);
					pairSt.setStatus(Status.Smsfetch);
					taskCompleter.addTask(() -> 
						SmsTask.get(telnetHelper, ch, ch.getCard(), pair, pair.getCard()), 
						new TaskDescriptor(SmsTask.class, st, new Date()));
				}	
			}
		}	
		
	}
	
	@Scheduled(fixedRate = 600000)
	public void checkBanks() {
//		logger.debug("Start checkBanks ...");
		for (Bank b: bankRepo.findAll()) {
			BankState st = bankRepo.findStateById(b.getId());
			Callable<State> checkBank = () -> BankStatus.get(b);
			taskCompleter.addTask(checkBank, new TaskDescriptor(BankStatus.class, st, new Date()));
		}
	}
	
	@Scheduled(cron = "0 0 0 * * *") 
	public void midnightReset() {
		logger.debug("Midnight resetting");
		cards.findAll().stream()
		.peek((c-> c.getStat(cards).resetDaylyCounters()))
		.peek(c-> c.refreshDayLimit())
		.forEach(c-> cards.makePersistent(c));
	}
	
	

}
