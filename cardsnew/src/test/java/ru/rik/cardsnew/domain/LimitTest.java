package ru.rik.cardsnew.domain;



import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.rik.cardsnew.ConfigJpaLite;
import ru.rik.cardsnew.db.CardRepo;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigJpaLite.class)
public class LimitTest {
	 @Autowired CardRepo cardRepo;	
	
//	@Test
//	@Rollback(false)
	
	public void t1() {
		cardRepo.refreshLimits();
//		cardRepo.findAll().stream()
//		.peek(c -> c.refreshDayLimit())
//		.forEach(c -> System.out.println(c.getName() + " " + c.getDlimit()));
//		for (Card c: cardRepo.findAll()) {
//			c.refreshDayLimit();
//			cardRepo.makePersistent(c);
//			System.out.println(c.getName() + " " + c.getDlimit());
//		}	
	}

	

//	public static void main(String[] args) {
//		ApplicationContext ctx = new AnnotationConfigApplicationContext(ConfigJpaLite.class);
//		cardRepo = ctx.getBean(CardRepo.class);
//		LimitTest lt = new LimitTest();
//		lt.LimitT();
//	}
}
