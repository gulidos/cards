package ru.rik.cardsnew.service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.State;
import ru.rik.cardsnew.service.http.GsmState;
import ru.rik.cardsnew.service.http.HttpHelper;
import ru.rik.cardsnew.service.http.SimSet;
@Service
public class PeriodicTasks {
	private static final Logger logger = LoggerFactory.getLogger(AsyncTasks.class);		
	
	@Autowired AsyncTasks asyncTasks;
	@Autowired ChannelRepo chanRepo;
	@Autowired HttpHelper httpHelper;
	@Autowired TaskCompleter taskCompleter;

	public PeriodicTasks() {
		logger.info("Instantiate the PeriodicTasks ...");

	}
	
	@Scheduled(fixedRate = 30000)
	public void checkChannels() {
		logger.debug("Start checkChannels ...");
		Set<Channel> simSetJobs = new HashSet<>();
		
		for (Channel ch : chanRepo.findAll()) { //TODO fetch only active channels
			if (!ch.isEnabled()) continue;
			
			
			ChannelState st = chanRepo.findStateById(ch.getId());
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
				Channel pair = ch.getPair();
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
	

	

}
