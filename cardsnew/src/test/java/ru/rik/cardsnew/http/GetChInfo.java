package ru.rik.cardsnew.http;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.transaction.Transactional;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.rik.cardsnew.config.RootConfig;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.State;
import ru.rik.cardsnew.service.PeriodicTasks;
import ru.rik.cardsnew.service.http.GsmState;
import ru.rik.cardsnew.service.http.HttpHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GetChInfo {
	@Autowired ChannelRepo chans;
	@Autowired HttpHelper httpHelper;
	@Autowired PeriodicTasks periodicTasks;
	@Autowired CompletionService<State> completionService;
	
	public static long chanId = 1;

	public GetChInfo() {}

//	@Test
	@Transactional
	@Rollback(false)
	public void t1changeCard() {
		int count = 0;
		Map<Future<State>, Long> map = new HashMap<>();
		for (Channel ch : chans.findAll()) {
			
			Callable<State> checkGsm = new Callable<State>() {
				public GsmState call() throws Exception {
					return GsmState.get(ch);
				}
			};
			Future<State> f = completionService.submit(checkGsm);
			map.put(f, ch.getId());
			count++;
		}
		
		for (int i = 0; i < count; i++) {
			Future<State> f;
			try {
				f = completionService.take();
				State result = f.get();
				Class<?> c = result.getClass();
				GsmState g = (GsmState) result;
				System.out.println(map.get(f) + " status: " + g);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test @Transactional
	public void t2changeCard() throws InterruptedException {
		periodicTasks.checkChannels();
		Thread.currentThread().sleep(10000);
	}
}
