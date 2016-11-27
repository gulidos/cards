package ru.rik.cardsnew.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Util {

	public Util() {	}

	public static Date getNowMinusSec(int sec) {
		Instant instant = LocalDateTime.now()
				.minusSeconds(sec)
				.atZone(ZoneId.systemDefault())
				.toInstant();
		return Date.from(instant);
	}
	
	public static Date getNowPlusSec(int sec) {
		Instant instant = LocalDateTime.now()
				.plusSeconds(sec)
				.atZone(ZoneId.systemDefault())
				.toInstant();
		return Date.from(instant);
	}
	
	
	public static Date parseDate(String s, String pattern) {  //"yyyy-MM-dd HH:mm:ss"
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		Instant instant = LocalDateTime.parse(s, formatter)
				.atZone(ZoneId.systemDefault())
				.toInstant();
		return Date.from(instant);
	}
	
	
	public static RuntimeException launderThrowable(Throwable t) {
		if (t instanceof RuntimeException)
			return (RuntimeException) t;
		else if (t instanceof Error)
			throw (Error) t;
		else
			throw new IllegalStateException("Not unchecked", t);
	}
	
	public static long msecBetween(Date d1, Date d2) {
		return Math.abs(d2.getTime() - d1.getTime());
	}
	
	public static boolean isApproxEqual(Date d1, Date d2) {
		return msecBetween(d1, d2) < 10;
	}
	
	public static boolean isApproxEqual(Date d1, Date d2, int delta) {
		return msecBetween(d1, d2) < delta;
	}
	
}
