package ru.rik.cardsnew.domain;

import java.text.ParseException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.rik.cardsnew.domain.events.Cdr;

public class CardStateTest {
	
	@Before
	public void loadData() {
		
	}
	
	
	@Test
	public void newCardCreation() {
		
		Card c = new Card(1, 1, "c1", "111", null, "", null, null, 1, true, 50, 1981, "test", null, false, null);
		CardStat cs = new CardStat(c);
		
		Assert.assertTrue(cs.isFree());
		Assert.assertTrue(cs.getAcd() == 0);
		Assert.assertTrue(cs.getAsr() == 0);
		Assert.assertTrue(cs.getTodaySecTotal() == 0);
		Assert.assertTrue(cs.getTodayCalls() == 0);
	}

	@Test
	public void applyCdr() throws ParseException {
		TestUtils.loadCdrs();
		Card c = new Card(1, 1, "c1", "111", null, "", null, null, 1, true, 50, 1981, "test", null, false, null);
		CardStat cs = new CardStat(c);
		Cdr cdr = Cdr.builder().date(new Date()).src("11112").dst("9999").cardId(1).billsec(60).trunk("trnk1")
				.disp("ANSWERED").regcode("77").uniqueid("1234567892").channelId(2).build();
		cs.applyCdr(cdr);
		
		Assert.assertTrue(cs.getTodaySecTotal() == 60);
		Assert.assertTrue(cs.getTodayCalls() == 1);
		Assert.assertTrue(cs.getAcd() == 1.125);
		Assert.assertTrue(cs.getAsr() == 80);
	}
	
	public CardStateTest() {	}
}
