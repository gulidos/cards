package ru.rik.cardsnew.domain;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.rik.cardsnew.config.Settings;
import ru.rik.cardsnew.service.http.GsmState;
import ru.rik.cardsnew.service.http.SimSet;
@Data
@EqualsAndHashCode(callSuper=true)
public class ChannelState extends MyState {
	private long id;
	private String name;
	
	private AtomicInteger priority = new AtomicInteger(1);

	private volatile GsmState gsmstatus;
	private volatile SimSet simset;
	private volatile Date lastGsmUpdate = new Date(0); // set date the most old when create State 
	private volatile Date nextGsmUpdate = new Date(0);
	
	private volatile Status status = Status.Failed;
	
	public void applyGsmStatu(GsmState gs) {
		if (gs.isReady()) {
			nextGsmUpdate = Util.getNowPlusSec(Settings.NORMAL_CHECK_GSM_INTERVAL);
			status = Status.Ready;
		} else { 
			nextGsmUpdate = Util.getNowPlusSec(Settings.FAILED_CHECK_GSM_INTERVAL);
			status = Status.Failed;
		}	
		this.gsmstatus = gs; 
		this.lastGsmUpdate = gs.getUpdate();
		
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
	
	public String toWeb() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(String.format("%1$s = %2$s%n", "name", getName()));
		sb.append(String.format("%1$s = %2$s%n", "priority", priority.get()));
		sb.append(String.format("%1$s = %2$s%n", "lastGsmUpdate", lastGsmUpdate));
		sb.append(String.format("%1$s = %2$s%n", "status", status));
		sb.append(" \n");
		if (gsmstatus != null) {
			sb.append(String.format("%1$s = %2$s%n", "operator", gsmstatus.getOperator()));
			sb.append(String.format("%1$s = %2$s%n", "sigquality", gsmstatus.getSigquality()));
			sb.append(String.format("%1$s = %2$s%n", "status", gsmstatus.getStatus()));
		} 
		
		return sb.toString();
	}
	
	
	@Override
	public Class<?> getClazz() {return ChannelState.class;	}
	
	public enum Status {
		Ready, Failed, Unreach, Inchange, PeerInchange, Smsfetch, UssdRec;
	}

}
