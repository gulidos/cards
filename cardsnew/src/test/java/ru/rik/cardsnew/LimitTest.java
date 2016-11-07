package ru.rik.cardsnew;

import org.springframework.beans.factory.annotation.Autowired;

import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.domain.Limit;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = ConfigJpaLite.class)
public class LimitTest {
	@Autowired CardRepo repo;
	
	public LimitTest() {
		
	}

//	@Test
	public void t1() {
		for (Limit l: repo.getLimits())
			System.out.println(l.toString());
	}
	
}
