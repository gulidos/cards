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
}
