package ru.rik.cardsnew.db;

import java.text.ParseException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.ConfigJpaH2;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Util;
import ru.rik.cardsnew.domain.events.Cdr;
import ru.rik.cardsnew.domain.repo.Cdrs;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigJpaH2.class)

public class CardRepoTest {
	@Autowired private  CardRepo cards;
	@Autowired private  ChannelRepo chans;
	@Autowired private Cdrs cdrs;
	@Autowired private GroupRepo groups;
	
	
	public CardRepoTest() {	}

	@Before
	public void loadData() throws ParseException {
	
	}
	
	@After
	public void resetState() {
		for (int i = 1; i <= cards.getCount(); i++)
			cards.removeStateIfExists(i);
		cards.init();
	}
	
	@Test 
	public void checkCount() {
		long size1 = cards.findAll().size();
		long size2 = cards.getCount();
		System.out.println("size1: " + size1 + " size2: " + size2);
	}


	
	@Test @Transactional
	public void getAllAvailableForSwitching() {
		Grp g = groups.findById(7);
		Assert.assertNotNull(g);
		
		List<Card> list = cards.findAllAvailableForChannel(g);
		Assert.assertTrue(list.size() > 0);
	}
	
	
	@Test @Transactional
	public void getTheBestForSwitching() {
		Card c1 = cards.findById(1);
		c1.setDlimit(65);
		cards.makePersistent(c1);
		
		Grp g = groups.findById(7);
		
		Card c = cards.findTheBestInGroupForInsert(g);
		System.out.println(c);
		Assert.assertTrue(c.equals(c1));		
	}

	
	@Test @Transactional
	public void getIfAllCardsNotActiveTheBestForSwitchingNull() {
		Grp g = groups.findById(7);
		for (Card c: cards.findAllAvailableForChannel(g))
			c.setActive(false);
		Card c = cards.findTheBestInGroupForInsert(g);
		Assert.assertTrue(c == null);		
	}
	
	@Test @Transactional
	public void timeIsOutForOneCardItIsCountOut() throws ParseException {
		Card c1 = cards.findById(1);
		int remainsMinutes = c1.getStat(cards).getMinRemains();
		
		Cdr cdr = Cdr.builder().date(Util.getNowMinusSec(1200)).cardId(1).billsec(remainsMinutes *60)
				.disp("ANSWERED").regcode("9").uniqueid("1234567892").channelId(1).build();
		c1.getStat(cards).applyCdr(cdr, c1, chans.findById(1), cdrs);
		
		Assert.assertEquals(c1.getStat(cards).getMinRemains(), 0);
		
		Grp g = groups.findById(7);
		List<Card> list = cards.findAllAvailableForChannel(g);

		Assert.assertTrue(list.size() > 0);
	}

	
	
	
	
	
	}
