package ru.rik.cardsnew.domain;

import java.text.DecimalFormat;
import java.util.SortedMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.rik.cardsnew.config.Settings;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.domain.events.Cdr;
import ru.rik.cardsnew.domain.events.Disposition;
import ru.rik.cardsnew.domain.repo.Cdrs;


@EqualsAndHashCode(of={"id", "name"})
public class CardStat implements State {
	static Logger log = Logger.getLogger(CardStat.class);
    public final DecimalFormat df = new DecimalFormat("###.#");

	private long id;
	@Getter @Setter private CardRepo repo;
	private String name;
	private AtomicBoolean free = new AtomicBoolean(true);
	
	@Getter	private volatile int asr;
	@Getter private volatile double acd;
	@Getter private volatile int todayMinTotal;
	@Getter private volatile int todayMin;
	@Getter private volatile int todayOffnet;
	@Getter private volatile int todayMsk;
	private volatile AtomicInteger todayCalls = new AtomicInteger(0);
	

	public CardStat() {} //needed !! 
	
	public CardStat(Card card) {
		setCard(card);
	}
	
	public void setCard(Card card) {
		this.id = card.getId();
		this.name = card.getName();
		this.todayMinTotal =  this.todayMin = 0;
		this.acd  = this.asr = 0;
	}

	public void applyCdr(Cdr cdr, Card card, Channel ch, Cdrs cdrs) {
		if (cdr.isToday()) {
			todayMinTotal += cdr.getMin();
			todayCalls.incrementAndGet();
			todayMin += cdr.getMinOper();
			if (Settings.MSK_REGCODE.equals(cdr.getRegcode()))
				todayMsk += cdr.getMinOper();
			if (cdr.isOffnet(card, ch))
				todayOffnet += cdr.getMinOper();
		}
		cdrs.addCdr(cdr);
		
		SortedMap<String, Cdr>  lastCdrs= cdrs.findCdrByCards(cdr.getCardId(), true);
		
		calcAsr(lastCdrs);
		calcAcd(lastCdrs);

	}

	private void calcAsr(SortedMap<String, Cdr>  lastCdrs) {
		if (lastCdrs.size() == 0) {
			asr = 0;
			return;
		}	
		int total = 0;
		double answered = 0;
		for (String k : lastCdrs.keySet()) {
			Cdr cdr = lastCdrs.get(k);
			if (cdr.getDisposition() == Disposition.ANSWERED)
				answered++;
			total++;
			if (total == Settings.ASR_AFFECTED) 
				break;
		}
		asr =  (int) (answered/total * 100);
	}
	
	
	/** calculates Acd in minutes
	 * @param lastCdrs - map of cdrs via Card
	 */
	private void calcAcd(SortedMap<String, Cdr>  lastCdrs) {
		if (lastCdrs.size() == 0) {
			acd = 0;
			return;
		}	

		int answered = 0;
		double sum = 0;
		for (String k : lastCdrs.keySet()) {
			Cdr cdr = lastCdrs.get(k);
			if (cdr.getDisposition() == Disposition.ANSWERED) {
				answered++;
				sum +=cdr.getBillsec();
			}	
			
			if (answered == Settings.ACD_AFFECTED) 
				break;
		}
		acd =  sum / answered / 60; // return minutes
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
	

	
	public void resetDaylyCounters() {
		todayMinTotal = todayMin =  0;
		todayCalls.set(0);
	}

	public Card getCard() {
		return repo.findById(getId());
	}

	public int getMinRemains() {
		Card c = getCard();
		if (c.isMskSeparate())
			return (int) ((c.getDlimit() + c.getDlimit() /  100d * Settings.PERCENT_MSK_PLUS)  - todayMin);
		else 	
			return c.getDlimit() - todayMin;
	}
	
	
	public int getMinRemains(Route route) {
		Card c = getCard();
		if (route.getOper() == c.getGroup().getOper()) {
			if (route.getRegcode() == 77) 
				return getMinRemains();
			else 
				return c.getDlimit() - todayMin;
		} else { 														//offnet
			if (todayOffnet < Settings.MAX_OFFNET_MIN)
				return Settings.MAX_OFFNET_MIN - todayOffnet;
			else 
				return 0;
		}	
	}
	
	
	public int getMinRemains(Card c, Route route, Channel ch) {
		return c.getDlimit() - todayMin;
	}
	
	public String getAcdFormatted() {
		return df.format(acd);
	}

	
	public int getTodayCalls() {return todayCalls.get();}
	
	@Override public long getId() {return id;}
	@Override public String getName() {return name;}

	@Override public Class<?> getClazz() {return CardStat.class;}

	@Override public void setId(long id) {this.id = id;	}

	@Override public void setName(String name) {this.name = name;}


	@Override
	public String toString() {
		return "CardStat [id=" + id + ", name=" + name + ", free=" + free + "]";
	}

}
