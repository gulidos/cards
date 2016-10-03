package ru.rik.cardsnew.service;

import java.text.SimpleDateFormat;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.service.http.GsmState;
import ru.rik.cardsnew.service.http.HttpHelper;
@Service
public class PeriodicTasks {
	private static final Logger logger = LoggerFactory.getLogger(AsyncTasks.class);		
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	@Autowired AsyncTasks asyncTasks;
	@Autowired ChannelRepo chanRepo;
	@Autowired HttpHelper httpHelper;

	public PeriodicTasks() {
		logger.info("Instantiate the PeriodicTasks ...");

	}
	
	@Scheduled(fixedRate = 300000)
	public void checkChannels() {
		logger.debug("Start checkChannels ...");
		ConcurrentMap<Long, ChannelState> map = chanRepo.getStates();
		
		for (Long id: map.keySet()) {
			ChannelState st = map.get(id);
			Callable<Futurable> checkGsm = new Callable<Futurable>() {
				public GsmState call() throws Exception {
					return httpHelper.getGsmState(st);
				}
			};
			Future<Futurable> f = completionService.submit(checkGsm);
			map.put(f, ch);
			count++;
		}
			
//		for (Channel ch : chanRepo.findAll()) {
//			asyncTasks.checkChannel(ch);
//		}
	}
	

	

}
