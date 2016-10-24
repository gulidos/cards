package ru.rik.cardsnew.domain;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.rik.cardsnew.domain.events.Cdr;


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
