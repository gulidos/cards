package ru.rik.cardsnew.db;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.CardStat;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Oper;
import ru.rik.cardsnew.domain.Place;
import ru.rik.cardsnew.domain.Util;
import ru.rik.cardsnew.domain.events.Cdr;
import ru.rik.cardsnew.domain.repo.Cdrs;

public class CardRepoTest {
	private CardRepo repo;
	private Grp g;
	private Bank b;
	private Cdrs cdrs;
	
	public CardRepoTest() {
	}

	@Before
	public void loadData() throws ParseException {
		initData();
	}

	@Test
	public void getCards() {
		Assert.assertTrue(repo.findAll().size() == 8);
	}
	
	@Test
	public void getAllAvailableForSwitching() {
		List<Card> list = repo.findAllAvailableForChannel(g);
		System.out.println(list.size());
		for (Card c: list)
			System.out.println(c.getName() + " " + c.getStat(repo).getMinRemains());
	}
	
	@Test
	public void getTheBestForSwitching() {
		Card c = repo.findTheBestInGroupForInsert(g);
		Assert.assertTrue(c.getId() == 2);		
	}
	
	
	@Test
	public void getTheBestForSwitchingNull() {
		for (Card c: g.getCards())
			c.setActive(false);
		Card c = repo.findTheBestInGroupForInsert(g);
		Assert.assertTrue(c == null);		
	}
	
	private void initData() throws ParseException {
		cdrs = new Cdrs();
		cdrs.init();
		repo = spy(new CardRepoImpl());

		b = Bank.builder().id(1).name("9.9.9.9").build();
		g = Grp.builder().id(1).name("g1").oper(Oper.RED).build();
		Set<Card> set = new HashSet<>();
		g.setCards(set);
		Channel ch =  Channel.builder().group(g).id(2).name("ch2").build();
		
		Cdr cdr = Cdr.builder().cardId(1).billsec(300).disp("ANSWERED").date(Util.getNowMinusSec(1000)).build();
		
		Card c1 = Card.builder().id(1).name("c1").active(true).bank(b).group(g).place(Place.b0000000).dlimit(10).build();
		CardStat cs = repo.addStateIfAbsent(c1);
		cs.applyCdr(cdr, c1, ch, cdrs);
		set.add(c1);
		doReturn(c1).when(repo).findById(1);

		
		Card c2 = Card.builder().id(2).name("c2").active(true).bank(b).group(g).place(Place.b0000001).dlimit(10).build();
		cs = repo.addStateIfAbsent(c2);
		set.add(c2);
		doReturn(c2).when(repo).findById(2);
		
		Card c3 = Card.builder().id(3).name("c3").active(true).bank(b).group(g).place(Place.b0000002).dlimit(10).build();
		cs = repo.addStateIfAbsent(c3);
		set.add(c3);
		doReturn(c3).when(repo).findById(3);

		
		Card c4 = Card.builder().id(4).name("c4").active(true).bank(b).group(g).place(Place.b0000003).dlimit(10).build();
		cs = repo.addStateIfAbsent(c4);
		set.add(c4);
		doReturn(c4).when(repo).findById(4);

		
		Card c5 = Card.builder().id(5).name("c5").active(true).bank(b).group(g).place(Place.b0000004).dlimit(10).build();
		cs = repo.addStateIfAbsent(c5);
		set.add(c5);
		doReturn(c5).when(repo).findById(5);

		
		Card c6 = Card.builder().id(6).name("c6").active(true).bank(b).group(g).place(Place.b0000005).dlimit(10).build();
		cs = repo.addStateIfAbsent(c6);
		set.add(c6);
		doReturn(c6).when(repo).findById(6);

		
		Card c7 = Card.builder().id(7).name("c7").active(true).bank(b).group(g).place(Place.b0000006).dlimit(10).build();
		cs = repo.addStateIfAbsent(c7);
		set.add(c7);
		doReturn(c7).when(repo).findById(7);

		
		Card c8 = Card.builder().id(8).name("c8").active(true).bank(b).group(g).place(Place.b0000007).dlimit(10).build();
		cs = repo.addStateIfAbsent(c8);
		set.add(c8);
		doReturn(c8).when(repo).findById(8);

		
		doReturn(new ArrayList<Card>(g.getCards())).when(repo).findAll();
	}
}
