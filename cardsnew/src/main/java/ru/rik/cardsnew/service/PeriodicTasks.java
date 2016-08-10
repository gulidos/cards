package ru.rik.cardsnew.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

public class PeriodicTasks {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private static Random random = new Random();

	public PeriodicTasks() {
		
	}
	
//@Scheduled(fixedRate=10000)
    public void work1() {
		System.out.println(Thread.currentThread().getName() + " work1 The time is now " + dateFormat.format(new Date()));
		for(int i = 0; i < 25; i++)	 {
			System.out.println(Thread.currentThread().getName() + " start async " + i);
			printMsg(" Message" + i);
      }
    }
	
	@Scheduled(fixedRate=12000)
    public void work2() {
		System.out.println(Thread.currentThread().getName() + " work2 The time is now " + dateFormat.format(new Date()));
    }

	@Async
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
