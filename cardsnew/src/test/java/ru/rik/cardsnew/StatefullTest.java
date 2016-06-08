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
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.db.CardRepoImpl;
import ru.rik.cardsnew.db.GroupRepoImpl;
import ru.rik.cardsnew.db.JpaConfig;
import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Oper;
import ru.rik.cardsnew.domain.Place;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=JpaConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StatefullTest {	
	
	@Autowired
	CardRepoImpl cardRepo;
	@Autowired
	GroupRepoImpl grpRepo;
	
	
	static Map<String, Card> cards = new HashMap<>();
	static Card lastCard;
	static Set <Oper> opers = new LinkedHashSet<>();
	static Oper lastOper;
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
			opers.add(c.getOper());
			lastOper = c.getOper();
			banks.add(c.getBank());
			lastBank = c.getBank();
			lastGrp = c.getGroup();
//        	System.out.println(c.toString() + c.getBank().getIp() + c.getOper().getName() + c.getGroup().getName());
		}
	}
	
	@Test
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
        			+ c.getOper().getName() + c.getGroup().getName());
		}
		System.out.println("Banks: " + banks.toString());
	}
	
	@Test
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
		c.setOper(lastOper);
		c.setGroup(lastGrp);
		cards.put(c.getName(), c);
		cardRepo.makePersistent(c);
	}
}
