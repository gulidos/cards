package ru.rik.cardsnew.domain.events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lombok.Data;

@Data
public abstract class Event {
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final Date date;
	private final long cardId;
	
	public abstract String toString();
	
	
	public Event(String date, long card) throws ParseException {
		this.date = sdf.parse(date); 
		this.cardId = card;
	}
	
	public String getDate() {
		return sdf.format(date);
	}
	
	public boolean isToday() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date todayStart = c.getTime();
		return todayStart.before(date);
	}

	public boolean isNewer (long maxMsecs) {
		Date now = new Date();
		long msecs = now.getTime() - date.getTime();
		return msecs < maxMsecs;
	}
	
	public long getCardId() {
		return cardId;
	}

}
