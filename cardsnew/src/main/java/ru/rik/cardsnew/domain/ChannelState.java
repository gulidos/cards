package ru.rik.cardsnew.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.rik.cardsnew.config.Settings;
import ru.rik.cardsnew.service.http.GsmState;
import ru.rik.cardsnew.service.http.SimSet;
@Data
@EqualsAndHashCode
public class ChannelState implements MyState {
	private static final Logger logger = LoggerFactory.getLogger(ChannelState.class);		

	private long id;
	private String name;
	
	private AtomicInteger priority = new AtomicInteger(1);

	private volatile GsmState gsmstatus;
	private volatile Date lastGsmUpdate = new Date(0); // set date the most old when create State 
	private volatile Date nextGsmUpdate = new Date(0);
	
	private volatile SimSet simset;
	private volatile Date lastSimSetUpdate = new Date(0);
	private volatile Date nextSimSetUpdate = new Date(0);
	
	private volatile Status status = Status.Failed;
	private static final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

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
	
	public void applySimSet(SimSet s) {
		logger.debug("apply to {} SimStatus {}" ,name ,s.toString());
		simset = s;
		nextSimSetUpdate = Util.getNowPlusSec(Settings.NORMAL_CHECK_GSM_INTERVAL);
		lastSimSetUpdate = new Date();
	}
	
	
	public boolean isGsmDateFresh() {
		return isDateFresh(nextGsmUpdate);
	}
	
	public boolean isSimSetDateFresh() {
		return isDateFresh(nextSimSetUpdate);
	}
	private boolean isDateFresh(Date nextdate) {
		long now = new Date().getTime();
		return now - nextdate.getTime() < 0;
	}
	

	
	public void incPriority() {priority.incrementAndGet();	}
	public int getPriority() {	return priority.get();	}
	
	public String toWeb() {
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("%1$s = %2$s%n", "lastGsmUpdate", df.format(lastGsmUpdate)));
		sb.append(String.format("%1$s = %2$s%n", "name", getName()));
		sb.append(String.format("%1$s = %2$s%n", "priority", priority.get()));
		sb.append(String.format("%1$s = %2$s%n", "status", status));
		sb.append(" \n");
		if (gsmstatus != null) {
			sb.append(String.format("%1$s = %2$s%n", "operator", gsmstatus.getOperator()));
			sb.append(String.format("%1$s = %2$s%n", "sigquality", gsmstatus.getSigquality()));
			sb.append(String.format("%1$s = %2$s%n", "status", gsmstatus.getStatus()));
		} 
		if (simset != null) {
			sb.append(" \n");
			sb.append(String.format("%1$s = %2$s%n", "lastSimSetUpdate", df.format(lastSimSetUpdate)));
			sb.append(String.format("%1$s = %2$s%n", "bank", simset.getBankIp()));
			sb.append(String.format("%1$s = %2$s%n", "cardPos", simset.getCardPos()));
		}
			
		return sb.toString();
	}
	
	
	@Override
	public Class<?> getClazz() {return ChannelState.class;	}
	
	public enum Status {
		Ready, Failed, Unreach, Inchange, PeerInchange, Smsfetch, UssdRec;
	}

}
