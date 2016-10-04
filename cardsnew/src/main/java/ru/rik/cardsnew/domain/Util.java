package ru.rik.cardsnew.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
	
	public static RuntimeException launderThrowable(Throwable t) {
		if (t instanceof RuntimeException)
			return (RuntimeException) t;
		else if (t instanceof Error)
			throw (Error) t;
		else
			throw new IllegalStateException("Not unchecked", t);
	}
}
