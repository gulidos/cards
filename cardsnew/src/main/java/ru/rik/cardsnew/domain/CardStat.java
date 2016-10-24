package ru.rik.cardsnew.domain;

import java.util.SortedMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.rik.cardsnew.config.Settings;
import ru.rik.cardsnew.domain.events.Cdr;
import ru.rik.cardsnew.domain.events.Disposition;
import ru.rik.cardsnew.domain.repo.Cdrs;


@EqualsAndHashCode(of={"id", "name"})
public class CardStat implements State {
	static Logger log = Logger.getLogger(CardStat.class);
	private long id;
	private String name;
	private AtomicBoolean free = new AtomicBoolean(true);
	@Getter	private int asr;
	@Getter private double acd;
	@Getter private int todaySecTotal;
	@Getter private int todayCalls;
	

	
	public CardStat(Card card) {
		this.id = card.getId();
		this.name = card.getName();
		this.todaySecTotal = this.todayCalls = 0;
		this.acd  = this.asr = 0;
		

	}

	public void applyCdr(Cdr cdr) {
		todaySecTotal +=cdr.getBillsec();
		todayCalls++;
		Cdrs.get().addCdr(cdr);
		SortedMap<String, Cdr>  lastCdrs= Cdrs.get().findCdrByCards(cdr.getCardId(), true);
		
		calcAsr(lastCdrs);
		calcAcd(lastCdrs);
//		for (String k: lastCdrs.keySet()) System.out.println(k + " " + lastCdrs.get(k));
//		System.out.println("asr: " + asr + " acd: " + acd);
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
		this.todaySecTotal = 0;
		this.todayCalls = 0;
	}

	

	

	
	@Override public long getId() {return id;}
	@Override public String getName() {return name;}

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
