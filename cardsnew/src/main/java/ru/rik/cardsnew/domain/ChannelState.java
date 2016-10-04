package ru.rik.cardsnew.domain;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.rik.cardsnew.config.Settings;
import ru.rik.cardsnew.service.http.GsmState;
@Data
@EqualsAndHashCode(callSuper=true)
public class ChannelState extends MyState {
	private long id;
	private String name;
	
	private AtomicInteger priority = new AtomicInteger(1);

	private volatile GsmState gsmstatus;
	private Date lastGsmUpdate = new Date(0); // set date the most old when create State 
	private Date nextGsmUpdate = new Date(0);
	
	private volatile Status status = Status.Ready;
	
	public void applyGsmStatu(GsmState gs) {
		if (gs.isReady()) {
			nextGsmUpdate = Util.getNowPlusSec(Settings.NORMAL_CHECK_GSM_INTERVAL);
			status = Status.Ready;
		} else { 
			nextGsmUpdate = Util.getNowPlusSec(Settings.FAILED_CHECK_GSM_INTERVAL);
			status = Status.Failed;
		}	
		this.gsmstatus = gs; //TODO is it safe?
		
	}	
	
	public boolean isGsmDateFresh() {
		return isDateFresh(nextGsmUpdate);
	}
	
	private boolean isDateFresh(Date nextdate) {
		long now = new Date().getTime();
		return now - nextdate.getTime() < 0;
	}
	

	
	public void incPriority() {priority.incrementAndGet();	}
	public int getPriority() {	return priority.get();	}

	public enum Status {
		Ready, Failed, Unreach, Inchange, PeerInchange;
	}

	@Override
	public Class<?> getClazz() {
		return ChannelState.class;
	}

}
