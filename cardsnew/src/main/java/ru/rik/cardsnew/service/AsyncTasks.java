package ru.rik.cardsnew.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.scheduling.annotation.Async;

public class AsyncTasks {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private static Random random = new Random();
	
	
	public AsyncTasks() {
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
