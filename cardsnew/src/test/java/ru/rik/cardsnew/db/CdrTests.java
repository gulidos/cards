package ru.rik.cardsnew.db;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Date;
import java.util.SortedMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.CardStat;
import ru.rik.cardsnew.domain.Util;
import ru.rik.cardsnew.domain.events.Cdr;
import ru.rik.cardsnew.domain.repo.Cdrs;

public class CdrTests {
	private Cdrs cdrs;
	private Card c;
	private CardStat cs;
	
	@Before
	public void loadData() throws ParseException {
		cdrs = loadCdrs();
		cdrs.init();
	}

	
	@Test 
	public void findCdrsofCard() {
		SortedMap<String, Cdr> subMap = cdrs.findCdrByCards(1, false);
		Assert.assertTrue(subMap.size() == 4);		
	}

	@Test 
	public void checkAsr () {
		
	}
	
	@Test 
	public void dateParsing() {
		String s = "2016-10-22 15:48:04.0";
		Date d = Util.parseDate(s, "yyyy-MM-dd HH:mm:ss.S");
		System.out.println(d);
	}

	public  Cdrs loadCdrs() throws ParseException {
		c = new Card(1, 1, "c1", "111", null, "", null, null, 1, true, 50, 1981, "test", null, false, null);
		CardRepo repo = mock(CardRepoImpl.class);
		when(repo.findById(1)).thenReturn(c);
		
		cs = Mockito.spy(CardStat.class);
		cs.setCard(c);
		cs.setRepo(repo);
		
		Cdrs cdrs = new Cdrs();
		cdrs.init();
		Cdr cdr = Cdr.builder().date(Util.getNowMinusSec(1000)).src("11111").dst("22222").cardId(1).billsec(0).trunk("trnk1")
				.disp("BUSY").regcode("77").uniqueid("1234567891").channelId(2).build();
		cs.applyCdr(cdr);
		
		cdr = Cdr.builder().date(Util.getNowMinusSec(1200)).src("11112").dst("22222").cardId(1).billsec(60).trunk("trnk1")
				.disp("ANSWERED").regcode("77").uniqueid("1234567892").channelId(2).build();
		cs.applyCdr(cdr);
		
		cdr = Cdr.builder().date(Util.getNowMinusSec(1300)).src("11112").dst("22222").cardId(1).billsec(70).trunk("trnk1")
				.disp("ANSWERED").regcode("77").uniqueid("1234567802").channelId(2).build();
		cs.applyCdr(cdr);
		
		cdr = Cdr.builder().date(Util.getNowMinusSec(1400)).src("11112").dst("22222").cardId(1).billsec(80).trunk("trnk1")
				.disp("ANSWERED").regcode("77").uniqueid("1234567803").channelId(2).build();
		cs.applyCdr(cdr);
		
		
		return cdrs;
	}

	public CdrTests() {	}
}
