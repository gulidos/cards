package ru.rik.cardsnew.domain;

import java.text.DecimalFormat;
import java.util.SortedMap;
import java.util.concurrent.atomic.AtomicBoolean;

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
	
	@Getter	private int asr;
	@Getter private double acd;
	@Getter private int todayMinTotal;
	@Getter private int todayMin;
	@Getter private int todayOffnet;
	@Getter private int todayMsk;
	@Getter private int todayCalls;
	

	public CardStat() {} //needed !! 
	
	public CardStat(Card card) {
		setCard(card);
	}
	
	public void setCard(Card card) {
		this.id = card.getId();
		this.name = card.getName();
		this.todayMinTotal = this.todayCalls  = this.todayMin = 0;
		this.acd  = this.asr = 0;
	}

	public void applyCdr(Cdr cdr, Card card, Channel ch) {
		if (cdr.isToday()) {
			todayMinTotal += cdr.getMin();
			todayCalls++;
			todayMin += cdr.getMinOper();
			if (Settings.MSK_REGCODE.equals(cdr.getRegcode()))
				todayMsk += cdr.getMinOper();
			if (cdr.isOffnet(card, ch))
				todayOffnet += cdr.getMinOper();
		}
		Cdrs.get().addCdr(cdr);
		
		SortedMap<String, Cdr>  lastCdrs= Cdrs.get().findCdrByCards(cdr.getCardId(), true);
		
		calcAsr(lastCdrs);
		calcAcd(lastCdrs);

	}

	private void calcAsr(SortedMap<String, Cdr>  lastCdrs) {
		if (lastCdrs.size() == 0) {
			asr = 0;
			return;
		}	
		double total = 0;
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
		todayMinTotal = todayMin = todayCalls= 0;
	}

	public Card getCard() {
		return repo.findById(getId());
	}

	public int getMinRemains(Route route) {
		return getCard().getDlimit() - todayMin;
	}
	
	public String getAcdFormatted() {
		return df.format(acd);
	}

	
	
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
