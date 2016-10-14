package ru.rik.cardsnew.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GetChInfo {
	@Autowired ChannelRepo chans;
//	@Autowired CompletionService<State> completionService;
	
	public static long chanId = 1;

	public GetChInfo() {}

	@Test
	@Transactional
	@Rollback(false)
	public void t1changeCard() throws IOException {
		int count = 0;
		Map<Future<State>, Long> map = new HashMap<>();
		for (Channel ch : chans.findAll()) {
			
//			SimSet s = SimSet.get(ch);
//			System.out.println(s.toString());
//			Callable<State> check = new Callable<State>() {
//				public GsmState call() throws Exception {
//					return GsmState.get(ch);
//				}
//			};
//			Callable<State> check = new Callable<State>() {
//				public SimSet call() throws Exception {
//					SimSet s = SimSet.get(ch);
//					System.out.println(s.toString());
//					return s;
//					
//				}
//			};
//			Future<State> f = completionService.submit(check);
//			map.put(f, ch.getId());
//			count++;
		}
		
		System.out.println("exit");
		System.exit(0);
//		for (int i = 0; i < count; i++) {
//			Future<State> f;
//			try {
//				f = completionService.take();
//				State result = f.get();
//				System.out.println("State is " + result.getClazz());
//				Class<?> c = result.getClass();
////				GsmState g = (GsmState) result;
//				SimSet g = (SimSet) result;
//				System.out.println(map.get(f) + " status: " + g);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (ExecutionException e) {
//				e.printStackTrace();
//			}
//		}
	}
	

}
