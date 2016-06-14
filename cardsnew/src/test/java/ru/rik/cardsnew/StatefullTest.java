package ru.rik.cardsnew;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import ru.rik.cardsnew.db.BankRepoImpl;
import ru.rik.cardsnew.db.CardRepoImpl;
import ru.rik.cardsnew.db.ChannelRepoImpl;
import ru.rik.cardsnew.db.GroupRepoImpl;
import ru.rik.cardsnew.db.JpaConfig;
import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Place;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=JpaConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StatefullTest {	
	
	@Autowired
	CardRepoImpl cardRepo;
	@Autowired
	GroupRepoImpl grpRepo;

	@Autowired
	BankRepoImpl bankRepo;
	
	static Map<String, Card> cards = new HashMap<>();
	static Card lastCard;
	
	static Set <Bank> banks = new LinkedHashSet<>();
	static Bank lastBank;
	static Set <Grp> grps;
	static Grp lastGrp;
	public StatefullTest() {	}
	
	
	@Test
	@Transactional
	@Rollback(false)
	public void t10loadData() {
		System.out.println("=========================t10loadData==============");
		for (Card c : cardRepo.findAll()) {
			cards.put(c.getName(), c);
			lastCard = c;
			System.out.println(c.getOper());
		
			banks.add(c.getBank());
			lastBank = c.getBank();
			lastGrp = c.getGroup();
        	System.out.println(c.toString() + 
        			c.getBank().getIp() + 
        			c.getOper() + 
        			c.getGroup().getName());
		}
	}
	
//	@Test
	@Transactional
	@Rollback(false)
	public void t12remove() {
		System.out.println("=========================		t12remove  ==============");
		System.out.println("Deleting card " + lastCard.toString());
		Card cardToRemove = cardRepo.findById(lastCard.getId());
		cardRepo.makeTransient(cardToRemove);
		
		cards.remove(lastCard.getName());
	}

	@Test
	public void t2verify() {
		System.out.println("=========================t2verify==============");
		for (Card c : cards.values()) {
        	System.out.println(c.toString() + c.getBank().getLocation()
        			+ c.getOper() + c.getGroup().getName());
		}
		System.out.println("Banks: " + banks.toString());
	}
	
//	@Test
	@Transactional
	@Rollback(false)
	public void t3add() {
		System.out.println("=========================t3add==============");
		Card c = new Card();
		c.setName("sd2w123");
		c.setNumber("123344555");
		c.setPlace(Place.b0000008);
		c.setSernumber("65245624562456");
		c.setBank(lastBank);

		c.setGroup(lastGrp);
		cards.put(c.getName(), c);
		cardRepo.makePersistent(c);
	}
	
	@Autowired
	ChannelRepoImpl chRepo;
	@Autowired
	private PlatformTransactionManager tm;
	
	
	@Test
//	@Transactional
//	@Rollback(false)
	public void t4changeCard() {
		System.out.println("=========================t4changeCard==============");
		TransactionDefinition definition = new DefaultTransactionDefinition();
		TransactionStatus status = tm.getTransaction(definition);
		try {
			Channel ch = chRepo.findById(1L);
			Card oldC = ch.getCard();
			System.out.println("Old card: " + oldC);

			oldC.setChannel(null);
			Card newC = cardRepo.findById(2L);
			
			newC.setChannel(ch);
			ch.setCard(newC);
			
			tm.commit(status);
			
		} catch (Exception e) {
			tm.rollback(status);
			throw new RuntimeException(e);
		}
	}
	
}
