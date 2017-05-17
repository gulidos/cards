package ru.rik.cardsnew.db;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.rik.cardsnew.ConfigJpaH2;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Event;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigJpaH2.class)
@DirtiesContext(classMode=ClassMode.BEFORE_CLASS)
public class BalanceRepoTest {

	public BalanceRepoTest() {
	}
	@Autowired BalanceRepo balances;
	@Autowired CardRepo cards; 
	@Autowired EventRepo events;
	
	@Test
	public void findBalances() {
		Card c = cards.findById(1);
		Assert.assertEquals(balances.findByCardOrderByDateDesc(c).size(), 6);
		
	}
	
	@Test
	public void getEvents() {
		Card c = cards.findById(1);
		for (Event  e: events.findByCardOrderByDateDesc(c))
			System.out.println(e);
			
	}
	


}
