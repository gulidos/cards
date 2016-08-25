package ru.rik.cardsnew.domain.events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rik.cardsnew.domain.CardStat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public  class Event {
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Date date;
	private long cardId;
	
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

	public interface Executable {
		public void execute(CardStat cardStat);
	}
}
