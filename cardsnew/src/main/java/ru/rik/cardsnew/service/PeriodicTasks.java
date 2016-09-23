package ru.rik.cardsnew.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.domain.Channel;
@Service
public class PeriodicTasks {
	private static final Logger logger = LoggerFactory.getLogger(AsyncTasks.class);		
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	@Autowired AsyncTasks asyncTasks;
	@Autowired ChannelRepo chanRepo;

	public PeriodicTasks() {
		logger.info("Instantiate the PeriodicTasks ...");

	}
	
	@Scheduled(fixedRate = 300000)
	public void checkChannels() {
		logger.debug("Start checkChannels ...");

		for (Channel ch : chanRepo.findAll()) {
			asyncTasks.checkChannel(ch);
		}
	}
	
//	@Scheduled(fixedRate=12000)
    public void work2() {
		System.out.println(Thread.currentThread().getName() + " work2 The time is now " + dateFormat.format(new Date()));
    }


}
