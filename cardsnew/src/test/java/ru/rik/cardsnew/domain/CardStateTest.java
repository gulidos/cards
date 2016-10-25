package ru.rik.cardsnew.domain;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.CardRepoImpl;
import ru.rik.cardsnew.domain.events.Cdr;
import ru.rik.cardsnew.domain.repo.Cdrs;

public class CardStateTest {
	private Card c;
	private CardStat cs;
	
	@Before
	public void loadData() {
		
	}
	
	
	@Test
	public void newCardCreation() {
		c = new Card(1, 1, "c1", "111", null, "", null, null, 1, true, 50, 1981, "test", null, false, null);
		cs = new CardStat(c);
		Assert.assertTrue(cs.isFree());
		Assert.assertTrue(cs.getAcd() == 0);
		Assert.assertTrue(cs.getAsr() == 0);
		Assert.assertTrue(cs.getTodayMinTotal() == 0);
		Assert.assertTrue(cs.getTodayMinOper() == 0);
		Assert.assertTrue(cs.getTodayCalls() == 0);
		
	}

	
	@Test
	public void applyCdr() throws ParseException {
		loadCdrs();

		Cdr cdr = Cdr.builder().date(new Date()).src("11112").dst("9999").cardId(1).billsec(60).trunk("trnk1")
				.disp("ANSWERED").regcode("77").uniqueid("1234567892").channelId(2).build();
		cs.applyCdr(cdr);
		System.out.println("TodayMinTotal " + cs.getTodayMinTotal() + " TodayMinOper " + cs.getTodayMinOper() + " TodayCalls " 
		+ cs.getTodayCalls()  + " Acd " + cs.getAcd() + " Asr " + cs.getAsr() );
		Assert.assertTrue(cs.getTodayMinTotal() == 4);
		Assert.assertTrue(cs.getTodayMinOper() == 6);
		Assert.assertTrue(cs.getTodayCalls() == 5);
		Assert.assertTrue(cs.getAcd() == 1.125);
		Assert.assertTrue(cs.getAsr() == 80);
	}
	
	@Test
	public void resetCardState() throws ParseException {
		loadCdrs();
		cs.resetDaylyCounters();
		Assert.assertTrue(cs.getTodayMinTotal() == 0);
		Assert.assertTrue(cs.getTodayMinOper() == 0);
		Assert.assertTrue(cs.getTodayCalls() == 0);
	}
	
	@Test
	public void subtractionMinutesTest() throws ParseException {
		loadCdrs();
		Assert.assertTrue(cs.getMinRemains() == 45);
		cs.applyCdr(Cdr.builder().date(new Date()).src("11112").dst("9999").cardId(1).billsec(2700).trunk("trnk1")
		.disp("ANSWERED").regcode("77").uniqueid("1234567892").channelId(2).build());
		Assert.assertTrue(cs.getMinRemains() == 0);
	}
	
	@Test
	public void addYesterdaysCdr() throws ParseException {
		loadCdrs();
		int secsAgo = 24 * 60 * 60 + 10;
		int todayCalls = cs.getTodayCalls();
		int minTotal = cs.getTodayMinTotal();
		int minOper = cs.getTodayMinOper();
		
		cs.applyCdr(Cdr.builder().date(Util.getNowMinusSec(secsAgo)).src("11112").dst("9999").cardId(1).billsec(2700)
				.trunk("trnk1").disp("ANSWERED").regcode("77").uniqueid("1234567892").channelId(2).build());
		Assert.assertTrue(cs.getTodayCalls() == todayCalls);
		Assert.assertTrue(cs.getTodayMinTotal() == minTotal);
		Assert.assertTrue(cs.getTodayMinOper() == minOper);
	}
	
	public CardStateTest() {	}
	
	
	
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
}
