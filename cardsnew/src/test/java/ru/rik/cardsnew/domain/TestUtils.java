package ru.rik.cardsnew.domain;

import java.text.ParseException;

import ru.rik.cardsnew.domain.events.Cdr;
import ru.rik.cardsnew.domain.repo.Cdrs;

public class TestUtils {

	public static Cdrs loadCdrs() throws ParseException {
		Cdrs cdrs = new Cdrs();
		cdrs.init();
		cdrs.addCdr(Cdr.builder().date(Util.getNowMinusSec(1000)).src("11111").dst("22222").cardId(1).billsec(0).trunk("trnk1")
				.disp("BUSY").regcode("77").uniqueid("1234567891").channelId(2).build());
		
		cdrs.addCdr(Cdr.builder().date(Util.getNowMinusSec(1200)).src("11112").dst("22222").cardId(1).billsec(60).trunk("trnk1")
				.disp("ANSWERED").regcode("77").uniqueid("1234567892").channelId(2).build());
		
		cdrs.addCdr(Cdr.builder().date(Util.getNowMinusSec(1300)).src("11112").dst("22222").cardId(1).billsec(70).trunk("trnk1")
				.disp("ANSWERED").regcode("77").uniqueid("1234567802").channelId(2).build());
		
		cdrs.addCdr(Cdr.builder().date(Util.getNowMinusSec(1400)).src("11112").dst("22222").cardId(1).billsec(80).trunk("trnk1")
				.disp("ANSWERED").regcode("77").uniqueid("1234567803").channelId(2).build());
		
		cdrs.addCdr(Cdr.builder().date(Util.getNowMinusSec(1500)).src("11113").dst("22224").cardId(2).billsec(90).trunk("trnk1")
				.disp("ANSWERED").regcode("77").uniqueid("1234567893").channelId(3).build());
		
		cdrs.addCdr(Cdr.builder().date(Util.getNowMinusSec(1600)).src("11114").dst("22224").cardId(2).billsec(100).trunk("trnk1")
				.disp("ANSWERED").regcode("77").uniqueid("1234567894").channelId(3).build());
		return cdrs;
	}
	
	

}
