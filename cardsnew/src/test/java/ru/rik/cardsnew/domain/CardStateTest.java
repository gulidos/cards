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
	private Channel ch;
	private Grp g;
	
	@Before
	public void loadData() {
		
	}
	
	
	@Test
	public void newCardCreation() {
		c = new Card(1, 1, "c1", "111", null, "", null, null, 1, true, 50, 1981, "test", null, false, null, new Limit(), true, true, null);
		cs = new CardStat(c);
		g = Grp.builder().id(1).name("g1").oper(Oper.RED).build(); 
		ch =  Channel.builder().group(g).id(2).name("ch2").build();

		Assert.assertTrue(cs.isFree());
		Assert.assertTrue(cs.getAcd() == 0);
		Assert.assertTrue(cs.getAsr() == 0);
		Assert.assertTrue(cs.getTodayMinTotal() == 0);
		Assert.assertTrue(cs.getTodayMin() == 0);
		Assert.assertTrue(cs.getTodayCalls() == 0);
		
	}

	
	@Test
	public void applyCdr() throws ParseException {
		Cdrs cdrs = loadCdrs();

		Cdr cdr = Cdr.builder().date(new Date()).src("11112").dst("9999").cardId(1).billsec(60).trunk("trnk1")
				.disp("ANSWERED").regcode("77").uniqueid("1234567892").channelId(2).build();
		cs.applyCdr(cdr, c, ch, cdrs);
		System.out.println("TodayMinTotal " + cs.getTodayMinTotal() + " TodayMinOper " + cs.getTodayMin() + " TodayCalls " 
		+ cs.getTodayCalls()  + " Acd " + cs.getAcd() + " Asr " + cs.getAsr() );
		Assert.assertTrue(cs.getTodayMinTotal() == 4);
		Assert.assertTrue(cs.getTodayMin() == 6);
		Assert.assertTrue(cs.getTodayCalls() == 5);
		Assert.assertTrue(cs.getAcd() == 1.125);
		Assert.assertTrue(cs.getAsr() == 80);
	}
	
	@Test
	public void resetCardState() throws ParseException {
		loadCdrs();
		cs.resetDaylyCounters();
		Assert.assertTrue(cs.getTodayMinTotal() == 0);
		Assert.assertTrue(cs.getTodayMin() == 0);
		Assert.assertTrue(cs.getTodayCalls() == 0);
	}
	
	@Test
	public void subtractionMinutesTest() throws ParseException {
		Cdrs cdrs = loadCdrs();
		c.setMskSeparate(false);
		Assert.assertTrue(cs.getMinRemains() == 45);
		cs.applyCdr(Cdr.builder().date(new Date()).src("11112").dst("9999").cardId(1).billsec(2700).trunk("trnk1")
		.disp("ANSWERED").regcode("77").uniqueid("1234567892").channelId(2).build(), c, ch, cdrs);
		Assert.assertTrue(cs.getMinRemains() == 0);
	}
	
	@Test
	public void subtractionMinutesMskSeparateTest() throws ParseException {
		Cdrs cdrs =  loadCdrs();
		c.setMskSeparate(true);
		Assert.assertTrue(cs.getMinRemains() == 60);
		cs.applyCdr(Cdr.builder().date(new Date()).src("11112").dst("9999").cardId(1).billsec(3600).trunk("trnk1")
		.disp("ANSWERED").regcode("77").uniqueid("1234567892").channelId(2).build(), c, ch, cdrs);
		Assert.assertTrue(cs.getMinRemains() == 0);
	}
	
	@Test
	public void addYesterdaysCdr() throws ParseException {
		Cdrs cdrs = loadCdrs();
		int secsAgo = 24 * 60 * 60 + 10;
		int todayCalls = cs.getTodayCalls();
		int minTotal = cs.getTodayMinTotal();
		int minOper = cs.getTodayMin();
		
		cs.applyCdr(Cdr.builder().date(Util.getNowMinusSec(secsAgo)).src("11112").dst("9999").cardId(1).billsec(2700)
				.trunk("trnk1").disp("ANSWERED").regcode("77").uniqueid("1234567892").channelId(2).build(), c, ch, cdrs);
		Assert.assertTrue(cs.getTodayCalls() == todayCalls);
		Assert.assertTrue(cs.getTodayMinTotal() == minTotal);
		Assert.assertTrue(cs.getTodayMin() == minOper);
	}
	
	public CardStateTest() {	}
	
	
	
	public  Cdrs loadCdrs() throws ParseException {
		g = Grp.builder().id(1).name("g1").oper(Oper.RED).build();
		c = new Card(1, 1, "c1", "111", null, "", g, null, 1, true, 50, 1981, "test", null, 
				false, null, new Limit(), true, true, null); 
		ch =  Channel.builder().group(g).id(2).name("ch2").build();
		CardRepo repo = mock(CardRepoImpl.class);
		when(repo.findById(1)).thenReturn(c);
		
		cs = Mockito.spy(CardStat.class);
		cs.setCard(c);
		cs.setRepo(repo);
		
		Cdrs cdrs = new Cdrs();
		cdrs.init();
		Cdr cdr = Cdr.builder().date(Util.getNowMinusSec(1000)).src("11111").dst("22222").cardId(1).billsec(0).trunk("trnk1")
				.disp("BUSY").regcode("77").uniqueid("1234567891").channelId(2).build();
		cs.applyCdr(cdr, c, ch, cdrs);
		
		cdr = Cdr.builder().date(Util.getNowMinusSec(1200)).src("11112").dst("22222").cardId(1).billsec(60).trunk("trnk1")
				.disp("ANSWERED").regcode("77").uniqueid("1234567892").channelId(2).build();
		cs.applyCdr(cdr, c, ch, cdrs);
		
		cdr = Cdr.builder().date(Util.getNowMinusSec(1300)).src("11112").dst("22222").cardId(1).billsec(70).trunk("trnk1")
				.disp("ANSWERED").regcode("77").uniqueid("1234567802").channelId(2).build();
		cs.applyCdr(cdr, c, ch, cdrs);
		
		cdr = Cdr.builder().date(Util.getNowMinusSec(1400)).src("11112").dst("22222").cardId(1).billsec(80).trunk("trnk1")
				.disp("ANSWERED").regcode("77").uniqueid("1234567803").channelId(2).build();
		cs.applyCdr(cdr, c, ch, cdrs);
		
		return cdrs;
	}
}
