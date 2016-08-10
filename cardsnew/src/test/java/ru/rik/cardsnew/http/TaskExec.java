package ru.rik.cardsnew.http;

import java.util.Random;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//https://spring.io/guides/gs/async-method/
//http://docs.spring.io/spring/docs/current/spring-framework-reference/html/scheduling.html
//https://www.keyup.eu/en/blog/101-synchronous-and-asynchronous-spring-events-in-one-application

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Test1Config.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TaskExec {
	
	@Autowired private ThreadPoolTaskExecutor taskExecutor;
	@Autowired private ThreadPoolTaskScheduler scheduler;
	
	private static Random random = new Random();


	@Test
	public void t1changeCard() {
		System.out.println("======================t1ch====================");
//		for(int i = 0; i < 25; i++) {
////            taskExecutor.execute(new MessagePrinterTask("Message" + i));
//			tasks.printMsg(" Message" + i);
//        }
		
		for (int i = 0; i < 250; i++) {
			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
			System.out.println("================taskExecutor: active threads: " + taskExecutor.getActiveCount()
			+ " getPoolSize: " + taskExecutor.getPoolSize()
			+ " CorePoolSize: " + taskExecutor.getCorePoolSize()
			);
//			System.out.println("================scheduler: active threads: " + scheduler.getActiveCount());
					
		}
//		
	}
	


}
