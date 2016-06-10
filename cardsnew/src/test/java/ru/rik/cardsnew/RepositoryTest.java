package ru.rik.cardsnew;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.db.CardRepoImpl;
import ru.rik.cardsnew.db.JpaConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=JpaConfig.class)
public class RepositoryTest {

	@Autowired
	CardRepoImpl cardsRepository;
	
	@Test
	@Transactional
//	@Ignore
	public void count() {
		System.out.println(" before inserting: " + cardsRepository.getCount());	
//		assertEquals(0L, cardsRepository.getCount());
	}
	
	@Test
	@Transactional
//	@Ignore
	public void addCard() {
//		Card card = new Card("mts111", "1.1.1.1", "b000001", "123123123", "9258762694");
//		Card saved = cardsRepository.save(card);
	}
	
	@Test
	@Transactional
//	@Ignore
	public void countAfter() {
		System.out.println(" after inserting: " + cardsRepository.getCount());
//		assertEquals(1, cardsRepository.getCount());
	}
}	
