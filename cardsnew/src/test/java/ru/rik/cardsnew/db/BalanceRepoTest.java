package ru.rik.cardsnew.db;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.rik.cardsnew.ConfigJpaH2;
import ru.rik.cardsnew.domain.Balance;
import ru.rik.cardsnew.domain.Card;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigJpaH2.class)
@DirtiesContext(classMode=ClassMode.BEFORE_CLASS)
public class BalanceRepoTest {

	public BalanceRepoTest() {
	}
	@Autowired BalanceRepo balances;
	@Autowired CardRepo cards; 
	
	@Test
	public void findBalances() {
		Card c = cards.findById(1);
		Assert.assertEquals(balances.findByCard(c).size(), 6);
		
	}
	
	@Test
	public void getPage() {
		Pageable page = new PageRequest(0, 4, Sort.Direction.DESC , "id");
		for (Balance b: balances.findByCard(page, cards.findById(1)))
			System.out.println(b);
		System.out.println("=====");
		for (Balance b: balances.findByCard(new PageRequest(1, 4, Sort.Direction.DESC , "id"), cards.findById(1)))
			System.out.println(b);
	}

}
