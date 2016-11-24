package ru.rik.cardsnew.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.ChannelState.Status;
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
	
	@Scheduled(fixedRate = 15000, initialDelay = 2000)
	public void checkChannels() {
		Set<Channel> pairsJobs = new HashSet<>();
		
		for (Channel ch : chans.findAll()) { 
			if (!ch.isEnabled()) continue;
			
			ChannelState st = chans.findStateById(ch.getId());
			if (!st.isGsmDateFresh())  {
				TaskDescr td = new TaskDescr(GsmState.class, st, new Date());
				taskCompleter.addTask(()-> GsmState.get(ch, td), td);
			}	

			if (pairsJobs.contains(ch)) { // if the channel was already requested as a pair
				pairsJobs.remove(ch);
			} else {
				Channel pair = ch.getPair(chans);
				Card pairCard = pair != null ? pair.getCard() : null;
				ChannelState pairSt = chans.findStateById(pair.getId());
				if (pair != null)
					pairsJobs.add(pair);
				if (!st.isSimSetDateFresh()) {
					TaskDescr td = new TaskDescr(SimSet.class, st, new Date());
					taskCompleter.addTask(()-> SimSet.get(ch, pair, td), td);
				}	
				if (!st.isSmsFetchDateFresh() && ch.getCard() != null) {
					st.setStatus(Status.Smsfetch);
					pairSt.setStatus(Status.Smsfetch);
					TaskDescr td = new TaskDescr(SmsTask.class, st, new Date());
					taskCompleter.addTask(() -> 
						SmsTask.get(telnetHelper, ch, ch.getCard(), pair, pairCard, td), td);
				}	
			}
		}	
		
	}
	
	@Scheduled(fixedRate = 600000, initialDelay = 3000)
	public void checkBanks() {
		logger.debug("Start checkBanks ...");
		for (Bank b: bankRepo.findAll()) {
			BankState st = bankRepo.findStateById(b.getId());
//			TaskDescr td = new TaskDescr(BankStatus.class, st, new Date());
			taskCompleter.addTask(() -> BankStatus.get(b, new TaskDescr(BankStatus.class, st, new Date())), 
					new TaskDescr(BankStatus.class, st, new Date()));
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
