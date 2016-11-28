package ru.rik.cardsnew.db;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.rik.cardsnew.ConfigJpaH2;
import ru.rik.cardsnew.domain.Balance;
import ru.rik.cardsnew.domain.CardStat;
import ru.rik.cardsnew.domain.Util;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigJpaH2.class)
public class BalanceTest {
	@Autowired private CardRepo cards;
	public BalanceTest() {	}
	
	@Test
	public void loadLastBalances () {
		cards.findLastBalances().forEach(b -> System.out.println(b));
		
	}
	
	@Test
	public void checkCardBalance() {
		CardStat c1 = cards.findStateById(1);
		Assert.assertEquals(c1.getBalance(), 112.0f, 0.1);
		Assert.assertTrue(Util.isApproxEqual(c1.getLastBalanceChecked(), new Date(), 10000));
		Assert.assertTrue(c1.getNextBalanceCheck().after(new Date()));
	}
	
	@Test
	public void checkNotExistingCardBalance() {
		CardStat c1 = cards.findStateById(10); //there aren't balances in db yet
		Assert.assertEquals(c1.getBalance(), 0, 0.1);
		Assert.assertTrue(Util.isApproxEqual(c1.getLastBalanceChecked(), new Date(0), 10000));
		Assert.assertTrue(Util.isApproxEqual(c1.getNextBalanceCheck(), new Date(0), 1000));
	}
	
	@Test
	public void checkThereArentBalancesForNotActiveCards() {
		CardStat c1 = cards.findStateById(28); //not active
		Assert.assertNull(c1);
	}
	
	@Test
	public void applyBalanceTest() {
		CardStat c1 = cards.findStateById(3); 
		Assert.assertEquals(c1.getBalance(), 100, 0.1);
		c1.applyBalance(Balance.builder().date(new Date()).balance(50f)
				.card(cards.findById(c1.getId()))
				.payment(false)
				.build());
		Assert.assertEquals(c1.getBalance(), 50, 0.1);
		Assert.assertTrue(Util.isApproxEqual(c1.getLastBalanceChecked(), new Date(), 10));
		c1.setBalance(100);
	}
	
	
	@Test
	public void paymentReceived() {
		CardStat c1 = cards.findStateById(3); 
		Assert.assertEquals(c1.getBalance(), 100, 0.1);
		c1.applyBalance(Balance.builder().date(new Date()).balance(50f)
				.card(cards.findById(c1.getId()))
				.payment(true)
				.build());
		
		c1.setBalance(100);
	}
}
