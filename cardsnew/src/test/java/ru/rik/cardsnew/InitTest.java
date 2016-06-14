package ru.rik.cardsnew;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.rik.cardsnew.config.RootConfig;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.repo.Cards;
import ru.rik.cardsnew.domain.repo.Channels;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={RootConfig.class})
public class InitTest {

	@Autowired 	Cards cards;
	@Autowired 	Channels channels;
	
	@Test
	public void test() {
		for (Card c : cards.getMap().values()) {
			System.out.println(c.toString());
		}
		for (Channel c : channels.getMap().values()) {
			System.out.println(c.toString());
		}
	}
	
	@Test
	public void testChangeCard() {
		
	}
	
}
