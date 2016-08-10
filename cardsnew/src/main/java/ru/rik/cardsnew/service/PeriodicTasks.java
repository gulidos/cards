package ru.rik.cardsnew.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
@Service
public class PeriodicTasks {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private static Random random = new Random();
	
	@Autowired AsyncTasks asyncTasks;
	private int iter = 0;

	public PeriodicTasks() {
		
	}
	
	@Scheduled(fixedRate=5000)
    public void work1() {
		iter++;
		System.out.println(Thread.currentThread().getName() + " work1 The time is now " + dateFormat.format(new Date()));
		for(int i = 0; i < 25; i++)	 {
			asyncTasks.printMsg(" Message " + i + " iter: " + iter + " put: " + dateFormat.format(new Date()));
      }
    }
	
//	@Scheduled(fixedRate=12000)
    public void work2() {
		System.out.println(Thread.currentThread().getName() + " work2 The time is now " + dateFormat.format(new Date()));
    }


}
