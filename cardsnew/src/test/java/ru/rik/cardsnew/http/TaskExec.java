package ru.rik.cardsnew.http;

import java.util.Random;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Test1Config.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TaskExec {
	
	@Autowired private ThreadPoolTaskExecutor taskExecutor;
	private static Random random = new Random();


	@Test
	public void t1changeCard() {
		for(int i = 0; i < 25; i++) {
            taskExecutor.execute(new MessagePrinterTask("Message" + i));
        }
		
		for (int i = 0; i < 250; i++) {
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
//			System.out.println("ActiveCount: " + taskExecutor.getActiveCount() 
//				+" PoolSize: " + taskExecutor.getPoolSize()
//				+" CorePoolSize: " + taskExecutor.getCorePoolSize()
//				);
		}
		
	}
	
	
	private class MessagePrinterTask implements Runnable {

        private String message;

        public MessagePrinterTask(String message) {
            this.message = message;
        }

        public void run() {
        	int rnd = Math.abs(random.nextInt()) % 10000;
            System.out.println(message + " rnd: " + rnd);
            try {Thread.sleep(rnd);} catch (InterruptedException e) {e.printStackTrace();}
        }
    }

}
