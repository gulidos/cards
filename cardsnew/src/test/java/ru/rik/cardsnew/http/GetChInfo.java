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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.rik.cardsnew.config.RootConfig;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.service.Futurable;
import ru.rik.cardsnew.service.http.GsmState;
import ru.rik.cardsnew.service.http.HttpHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GetChInfo {
	@Autowired ChannelRepo chans;
	@Autowired HttpHelper httpHelper;
	@Autowired ThreadPoolTaskExecutor taskExecutor;
	@Autowired CompletionService<Futurable> completionService;
	
	public static long chanId = 1;

	public GetChInfo() {}

	@Test
	@Transactional
	@Rollback(false)
	public void t1changeCard() {
		int count = 0;
		Map<Future<Futurable>, Channel> map = new HashMap<>();
		for (Channel ch : chans.findAll()) {
			
			Callable<Futurable> checkGsm = new Callable<Futurable>() {
				public GsmState call() throws Exception {
					return httpHelper.getGsmState(ch);
				}
			};
			Future<Futurable> f = completionService.submit(checkGsm);
			map.put(f, ch);
			count++;
		}
		
		for (int i = 0; i < count; i++) {
			Future<Futurable> f;
			try {
				f = completionService.take();
				Futurable result = f.get();
				Class<?> c = result.getCalss();
				GsmState g = (GsmState) result;
				System.out.println(map.get(f) + " status: " + g);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			
			
		}
		

	}
}
