package ru.rik.cardsnew.domain;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.rik.cardsnew.config.Settings;
import ru.rik.cardsnew.domain.events.Cdr;
import ru.rik.cardsnew.domain.events.Disposition;
import ru.rik.cardsnew.domain.events.Event;

@NoArgsConstructor
@EqualsAndHashCode(of={"id", "name"})
public class CardStat implements State {
	static Logger log = Logger.getLogger(CardStat.class);
	private long id;
	private String name;
	private AtomicBoolean free = new AtomicBoolean(true);
	
	private Deque<Event> events;
	private int asr;
	private double acd;
	private int spread;
	private double todayMin;
	private int todayCalls;
	private int windowCalls;
	private int todayOperMin;
	private int mskOperMin;
	private int offnetOperMin;
	// this.startTime = System.currentTimeMillis() + delay; - time  for checking some status;
	
//	      public long getDelay(TimeUnit unit) {
//	          long diff = startTime - System.currentTimeMillis();
//	          return unit.convert(diff, TimeUnit.MILLISECONDS);
//	      }


	public CardStat(Card card) {
		this.id = card.getId();
		this.todayMin = 0;
		this.todayOperMin = 0;
		this.mskOperMin = 0;
		this.offnetOperMin = 0;
		this.events = new LinkedBlockingDeque<>();
	}


	public boolean isFree() {return free.get();	}
	
	/** true if successful     
	 * @param expect the expected value
     * @param update the new value
     * @return {@code true} if successful. False return indicates that
     * the actual value was not equal to the expected value. */
	public boolean setFree(boolean expect, boolean update) {
		return free.compareAndSet(expect, update);
	}
	
	private void incTodayTime(Cdr cdr) {
		if (cdr.isToday()) {
			this.todayMin += cdr.getMin();
			this.todayOperMin += cdr.getMinOper();
			if (Settings.MSK_REGCODE.equals(cdr.getRegcode()))
				this.mskOperMin += cdr.getMinOper();
			try {
				if (cdr.isOffnet())
					this.offnetOperMin += cdr.getMinOper();
			} catch (Exception e) {
				log.error(e, e);
			}
			this.todayCalls++;
		}
	}
	
	public void resetDaylyCounters() {
		this.todayMin = 0;
		this.todayOperMin = 0;
		this.mskOperMin = 0;
		this.offnetOperMin = 0;
		this.todayCalls = 0;
	}

	
//	public String toString() {
//		StringBuffer sb = new StringBuffer();
//		sb.append(getId());
//		sb.append(" today:").append(todayOperMin).append("(").append(getTodayMin()).append(")");
//		sb.append(":").append(mskOperMin);
//		sb.append(":").append(offnetOperMin);
//		sb.append("-").append(todayCalls);
//		sb.append(" 24h:").append(getLast24hMinutes());
//		sb.append("-").append(getLast24hCallNumber());
//		sb.append(" 1h:").append(getLast1hMinutes());
//		sb.append("-").append(getLast1hCallNumber());
//		sb.append(" ASR:").append(getAsr());
//		sb.append(" ACD:").append(getAcd());
//		sb.append(" s:").append(Settings.d_int.format(getSpread()));
//		sb.append(":").append(Settings.d_int.format(getLast24hSpread()));
//		return sb.toString();
//	}


	public void calcAsr() {
		List<Cdr> lastCalls = getLastNCDRs(Settings.ASR_AFFECTED, null);
		if (lastCalls.size() > 0) {
			double successCalls = 0;
			for (Cdr cdr : lastCalls) {
				if (cdr.getDisposition() == Disposition.ANSWERED) {
					successCalls++;
				}
			}
			double asr = successCalls / Settings.ASR_AFFECTED * 100;
			setAsr((int) asr);
		} else {
			setAsr(0);
		}
	}

	public void calcAcd() {
		List<Cdr> lastCalls = getLastNCDRs(Settings.ASR_AFFECTED, Disposition.ANSWERED);
		if (lastCalls.size() > 0) {
			double sumSec = 0;
			for (Cdr cdr : lastCalls) {
				sumSec += cdr.getBillsec();
			}
			double acd = sumSec / Settings.ASR_AFFECTED / 60;
			setAcd(acd);
		} else {
			setAcd(0);
		}
	}

	public List<Cdr> getLastNCDRs(int k, Disposition disp) {
		List<Cdr> cdrs = new ArrayList<>();
		int count = events.size() < k ? events.size() : k;
		if (count == 0)
			return cdrs;
		int i = 0;
		for (Event event : events) {
			if (event instanceof Cdr) {
				Cdr cdr = (Cdr) event;
				if ((disp != null && cdr.getDisposition() == disp) || (disp == null)) {
					i++;
					cdrs.add(cdr);
				}
			}
			if (i >= count)
				break;
		}
		return cdrs;
	}
	

	
	public int removeOldEvents() {
		int n = 0;
		int freshCount = 0;
		long maxMsecs = 5760 * 60 * 1000;
		Iterator<Event> it = events.descendingIterator(); //от старых к новым
		while (it.hasNext()) {
			Event event = it.next();
			if (event.isNewer(maxMsecs)) {
				freshCount++;
				if (freshCount >= Settings.STOP_NUMBER)
					break;
			} else {
				it.remove();
				n++;
			}
		}
		return n;
	}
	
	public List<Event> getDiapasonEvents (long msecs) {
		int stopNumber = 0;
		List <Event> eventLst = new ArrayList<>();
		Iterator<Event> it = events.iterator();
		while (it.hasNext()) {
			Event event = it.next();
			if (event.isNewer(msecs)) {
				eventLst.add(event);
			} else {
				stopNumber++;
				if (stopNumber >= Settings.STOP_NUMBER)
					break;
			}
		}
		return eventLst;
	}
	
	public int getLast24hMinutes () {
		int n = 0;
		for (Event event: getDiapasonEvents (86400000)) { //24 * 60 * 60 * 1000
			Cdr cdr = null;
			if (event instanceof Cdr) 
				cdr = (Cdr) event;
			else break;
			n +=cdr.getMinOper();
		}
		return n;
	}
	
	public int getLast24hCallNumber () {
		int n = 0;
		for (Event event: getDiapasonEvents (86400000)) { //24 * 60 * 60 * 1000
			if (event instanceof Cdr) 
				n++;
			else break;
		}
		return n;
	}
	
	public double getLast24hSpread () {
		double uniqN = 0; //number of uniq Dst
		double m = 0; //whole number of Dst 
		Set <String> uniqNumbers = new HashSet<>();
		for (Event event: getDiapasonEvents (86400000)) { //24 * 60 * 60 * 1000
			if (event instanceof Cdr ) {
				if(uniqNumbers.add(((Cdr) event).getDst())) 
					uniqN++;
				m++;
			}		
		}
		double spread = uniqN / m * 100;
		return (int) spread;
	}
	
	public double getSpread () {
		double uniqN = 0; //number of uniq Dst
		double m = 0; //whole number of Dst 
		Set <String> uniqNumbers = new HashSet<>();
		for (Event event: getDiapasonEvents (345600000)) { //96 * 60 * 60 * 1000
			if (event instanceof Cdr ) {
				if(uniqNumbers.add(((Cdr) event).getDst())) 
					uniqN++;
				m++;
			}		
		}
		double spread = uniqN / m * 100;
		return (int) spread;
	}
	
	public int getLast1hMinutes () {
		int n = 0;
		for (Event event: getDiapasonEvents (3600000)) { //60 * 60 * 1000
			Cdr cdr = null;
			if (event instanceof Cdr) 
				cdr = (Cdr) event;
			else break;
			n +=cdr.getMinOper();
		}
		return n;
	}
	
	public int getLast1hCallNumber () {
		int n = 0;
		for (Event event: getDiapasonEvents (3600000)) { //60 * 60 * 1000
			if (event instanceof Cdr) 
				n++;
			else break;
		}
		return n;
	}

	public void setAsr(int asr) {
		this.asr = asr;
	}

	public int getAsr() {
		return this.asr;
	}
	
	@Override public long getId() {return id;}
//	@Override public void setId(long id) { this.id = id;}
	@Override public String getName() {return name;}
//	@Override public void setName(String name) {this.name = name;}

	public Deque<Event> getEvents() {
		return events;
	}

	public void setEvents(Deque<Event> events) {
		this.events = events;
	}

	public String getTodayMin() {
		return Settings.d_int.format(todayMin);
	}

	public void setTodayMin(int totalSec) {
		this.todayMin = totalSec;
	}

	public int getTodayOperMin() {
		return todayOperMin;
	}

	public void setTodayOperMin(int totalOperMin) {
		this.todayOperMin = totalOperMin;
	}

	public int getMskOperSec() {
		return mskOperMin;
	}

	public void setMskOperSec(int mskOperSec) {
		this.mskOperMin = mskOperSec;
	}

	public int getOffnetOperMin() {
		return offnetOperMin;
	}

	public void setOffnetOperMin(int offnetOperMin) {
		this.offnetOperMin = offnetOperMin;
	}

	public int getTodayCalls() {
		return todayCalls;
	}

	public void setTodayCalls(int todayCalls) {
		this.todayCalls = todayCalls;
	}

	public String getAcd() {
		return Settings.df.format(acd);
	}

	public void setAcd(double acd) {
		this.acd = acd;
	}

	public int getWindowCalls() {
		return windowCalls;
	}

	public void setWindowCalls(int windowCalls) {
		this.windowCalls = windowCalls;
	}

	@Override
	public Class<?> getClazz() {
		return CardStat.class;
	}

	@Override
	public void setId(long id) {this.id = id;	}

	@Override
	public void setName(String name) {this.name = name;}


	@Override
	public String toString() {
		return "CardStat [id=" + id + ", name=" + name + ", free=" + free + "]";
	}

}
