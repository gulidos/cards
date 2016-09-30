package ru.rik.cardsnew.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.service.http.HttpHelper;

public class AsyncTasks {
	private static final Logger logger = LoggerFactory.getLogger(AsyncTasks.class);		

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private static Random random = new Random();
	
	@Autowired HttpHelper httpHelper;
	public AsyncTasks() {
		logger.debug("Instantiate the AsyncTasks ...");
	}
	
	@Async("taskExecutor")
	public void checkChannel(Channel ch) {
		try {
			httpHelper.getGsmState(ch);
		} catch (Exception e) {
			logger.error(e.getMessage() + "trying to check channel " + ch.getName() + " ip: " + ch.getBox().getIp(), e);
		}
		
	}
	
	@Async("taskExecutor")
	public void printMsg(String message) {
		int rnd = Math.abs(random.nextInt()) % 10000;
		Date start = new Date();
        try {Thread.sleep(rnd);} catch (InterruptedException e) {e.printStackTrace();}
        
        System.out.println(Thread.currentThread().getName() +  message + " rnd: " + rnd 
        		+ " started at " +  dateFormat.format(start)
        		+ " ended at " +  dateFormat.format(new Date())
        		);
    }

}
