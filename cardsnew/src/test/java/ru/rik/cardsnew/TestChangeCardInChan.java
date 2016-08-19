package ru.rik.cardsnew;

import javax.transaction.Transactional;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.rik.cardsnew.db.CardRepoImpl;
import ru.rik.cardsnew.db.ChannelRepoImpl;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestChangeCardInChan {
//	@Autowired GroupRepoImpl groups;
//	@Autowired BoxRepoImpl boxes;
//	@Autowired TrunkRepoImpl trunks;
	@Autowired ChannelRepoImpl chans;
	@Autowired CardRepoImpl cards;

	public TestChangeCardInChan() {
	}

	public static Card oldCadr;
	public static long cardId = 2;
	public static long chanId = 1;
	
	@Test
	@Transactional
	@Rollback(false)
	public void t1changeCard() {
		System.out.println("=========================t1changeCard==============");

		Card c = cards.findById(cardId);
		System.out.println("Card before: " + c.toStringAll());
		Channel ch = chans.findById(chanId);
		oldCadr = ch.getCard();
		System.out.println("Channel before: " + ch.toString());
		ch.setCard(c);
//		c.setChannel(ch);
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void t2check() {
		System.out.println("=========================t2check==============");
		Channel ch = chans.findById(chanId);
//		Card c = ch.getCard();
		Card c = cards.findById(cardId);
 		System.out.println("Channel after: " + ch.toString());
		System.out.println("Card after: " + c.toStringAll());
	}	
	
	@Test
	@Transactional
	@Rollback(false)
	public void t3back() {
		System.out.println("=========================t2back==============");

		Channel ch = chans.findById(chanId);
		ch.setCard(oldCadr);
		chans.makePersistent(ch);
		System.out.println("Channel after back: " + ch.toString());

	}
	
}
