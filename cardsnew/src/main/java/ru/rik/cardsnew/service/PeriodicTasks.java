package ru.rik.cardsnew.service;

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
import ru.rik.cardsnew.domain.State;
import ru.rik.cardsnew.service.http.BankStatus;
import ru.rik.cardsnew.service.http.GsmState;
import ru.rik.cardsnew.service.http.HttpHelper;
import ru.rik.cardsnew.service.http.SimSet;
@Service
public class PeriodicTasks {
	private static final Logger logger = LoggerFactory.getLogger(PeriodicTasks.class);		
	
	@Autowired AsyncTasks asyncTasks;
	@Autowired ChannelRepo chans;
	@Autowired CardRepo cards;
	@Autowired BankRepo bankRepo;
	@Autowired HttpHelper httpHelper;
	@Autowired TaskCompleter taskCompleter;

	public PeriodicTasks() {
		logger.info("Instantiate the PeriodicTasks ...");

	}
	
	@Scheduled(fixedRate = 15000)
	public void checkChannels() {
//		logger.debug("Start checkChannels ...");
		Set<Channel> simSetJobs = new HashSet<>();
		
		for (Channel ch : chans.findAll()) { 
			if (!ch.isEnabled()) continue;
			
			ChannelState st = chans.findStateById(ch.getId());
			if (!st.isGsmDateFresh()) {
				Callable<State> checkGsm = new Callable<State>() {
					public GsmState call() throws Exception {
						return GsmState.get(ch);
					}
				};
				taskCompleter.addTask(checkGsm, st);
			}

			if (simSetJobs.contains(ch)) { // if the channel was already requested as a pair
				simSetJobs.remove(ch);
			} else {
				Channel pair = ch.getPair(chans);
				if (pair != null)
					simSetJobs.add(pair);
				if (!st.isSimSetDateFresh()) {
					Callable<State> checkSimSet = new Callable<State>() {
						public SimSet call() throws Exception {
							return SimSet.get(ch, pair);
						}
					};
					taskCompleter.addTask(checkSimSet, st);
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
			taskCompleter.addTask(checkBank, st);
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
