package ru.rik.cardsnew;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.db.CardsRepository;
import ru.rik.cardsnew.db.JpaConfig;
import ru.rik.cardsnew.domain.Card;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=JpaConfig.class)
public class RepositoryTest {

	@Autowired
	CardsRepository cardsRepository;
	
	@Test
	@Transactional
//	@Ignore
	public void count() {
		System.out.println(" before inserting: " + cardsRepository.count());	
		assertEquals(0, cardsRepository.count());
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
		System.out.println(" after inserting: " + cardsRepository.count());
		assertEquals(1, cardsRepository.count());
	}
}
