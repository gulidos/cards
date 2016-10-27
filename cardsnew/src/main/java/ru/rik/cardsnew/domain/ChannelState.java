package ru.rik.cardsnew.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
	private volatile Date lastGsmUpdate, nextGsmUpdate, lastSimSetUpdate, nextSimSetUpdate; 
	private volatile SimSet simset;
	@Getter
	private volatile Status status;
	private volatile Date lastStatusChange;
	
	private final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

	public ChannelState () {
		this.status = Status.Failed;
		this.lastStatusChange = new Date();
		this.lastGsmUpdate = new Date(0); // set date the oldest when create State 
		this.nextGsmUpdate = new Date(0);  
		this.nextSimSetUpdate = new Date(0);
		this.lastSimSetUpdate = new Date(0);	
	}
	
	public ChannelState (Status s, Date lastStatusChange ) {
		this.status = s;
		this.lastStatusChange = lastStatusChange;
		this.lastGsmUpdate = new Date(0); // set date the oldest when create State 
		this.nextGsmUpdate = new Date(0);  
		this.nextSimSetUpdate = new Date(0);
		this.lastSimSetUpdate = new Date(0);
	}
	
	public void applyGsmStatus(GsmState gs) {
		if (gs.isReady()) {
			nextGsmUpdate = Util.getNowPlusSec(Settings.NORMAL_CHECK_GSM_INTERVAL);
			setStatus(Status.Ready);
		} else {
			setStatus(Status.Failed);
		}
		this.gsmstatus = gs;
		this.lastGsmUpdate = gs.getUpdate();
	}
	
	public void applySimSet(SimSet s) {
//		logger.debug("apply to {} SimStatus {}" ,name ,s.toString());
		simset = s;
		nextSimSetUpdate = Util.getNowPlusSec(Settings.NORMAL_CHECK_GSM_INTERVAL);
		lastSimSetUpdate = new Date();
	}
	
	
	public void setStatus(Status s) {
		switch (s) {
		case Failed:	setFailedStatus(s);break;
		case Ready:		setReadyStatus(s); break;
		case Unreach:	setUnreachStatus(s); break;
		case Inchange: 
		case PeerInchange: setChangeStatus(s); break;
		case Smsfetch: 
		case UssdRec:	setMantainStatus(s); break;
		default:
			throw new IllegalArgumentException("Unknown channel's status: " + s.toString());
		}
	}

	private void setFailedStatus(Status s) {
		switch (status) {
		case Inchange:	case PeerInchange:
			if (!isStillHasToBeIniting()) {
				nextGsmUpdate = Util.getNowPlusSec(Settings.NORMAL_CHECK_GSM_INTERVAL);					
				this.status = s; //Become Failed
				lastStatusChange = new Date();
			} else 
				nextGsmUpdate = Util.getNowPlusSec(Settings.FAILED_CHECK_GSM_INTERVAL);
			break;
		case Failed:
			if (isStillHasToBeIniting())
				nextGsmUpdate = Util.getNowPlusSec(Settings.FAILED_CHECK_GSM_INTERVAL);
			else 
				nextGsmUpdate = Util.getNowPlusSec(Settings.NORMAL_CHECK_GSM_INTERVAL);
			break;
		case Ready:	case UssdRec: case Smsfetch: case Unreach:	 // has been Unreachable, become Failed
			this.status = s; //Become Failed
			lastStatusChange = new Date();
			nextSimSetUpdate = Util.getNowPlusSec(Settings.FAILED_CHECK_GSM_INTERVAL);
			break;
		default:
			break;
		}
	}
	
	private void setReadyStatus(Status s) {
		this.status = s;
		nextGsmUpdate = Util.getNowPlusSec(Settings.NORMAL_CHECK_GSM_INTERVAL);
		lastStatusChange = new Date();
	}
	
	
	private void setChangeStatus(Status s) {
		this.status = s;
		nextGsmUpdate = Util.getNowPlusSec(Settings.FAILED_CHECK_GSM_INTERVAL);
		lastStatusChange = new Date();
	}
	
	private void setUnreachStatus(Status s) {
		if (status == Status.Unreach || status == Status.Failed) {
			if(isStillHasToBeIniting()) 
				nextGsmUpdate = Util.getNowPlusSec(Settings.FAILED_CHECK_GSM_INTERVAL);
			else
				nextGsmUpdate = Util.getNowPlusSec(Settings.NORMAL_CHECK_GSM_INTERVAL);
		} else {
			lastStatusChange = new Date();
			nextGsmUpdate = Util.getNowPlusSec(Settings.FAILED_CHECK_GSM_INTERVAL);
		}
		this.status = s;
	}
	
	private void setMantainStatus(Status s) {
		if (status == Status.Ready) {
			this.status = s;
			lastStatusChange = new Date();
			nextGsmUpdate = Util.getNowPlusSec(Settings.NORMAL_CHECK_GSM_INTERVAL);
		}
	}
	
	public boolean isGsmDateFresh() {
		return isDateFresh(nextGsmUpdate);
	}
	
	public boolean isSimSetDateFresh() {
		return isDateFresh(nextSimSetUpdate);
	}
	
	public boolean isStillHasToBeIniting() {
		long maxTimeOfReady = lastStatusChange.getTime() + Settings.TIME_FOR_SWITCH * 1000;
		return isDateFresh(new Date(maxTimeOfReady));
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
		sb.append(String.format("%1$s = %2$s%n", "lastGsmUpdate", df.format(lastGsmUpdate)));
		sb.append(String.format("%1$s = %2$s%n", "nextGsmUpdate", df.format(nextGsmUpdate)));
		sb.append(String.format("%1$s = %2$s%n", "priority", priority.get()));
		sb.append(String.format("%1$s = %2$s %3$s %4$s%n", "status", status, "since", df.format(lastStatusChange)));
		sb.append(" \n");
		if (gsmstatus != null) {
			sb.append(String.format("%1$s = %2$s%n", "operator", gsmstatus.getOperator()));
			sb.append(String.format("%1$s = %2$s%n", "sigquality", gsmstatus.getSigquality()));
			sb.append(String.format("%1$s = %2$s%n", "status", gsmstatus.getStatus()));
		} 
//		Place place = Place.valueOf(simset.getCardPos());
//		Bank bank = BankRepoImpl.get().findByName(simset.getBankIp());
//		for (Bank b : BankRepoImpl.get().findAll()) {
//			logger.debug("bank {}, state {}", b.toString(), BankRepoImpl.get().findStateById(b.getId()));
//		}
//		logger.debug("place {} bank {}", place, bank.getId());
//		Card c = CardRepoImpl.get().findCardsByPlace(place, bank);
		
		if (simset != null) {
			sb.append(" \n");
			sb.append(String.format("%1$s = %2$s%n", "lastSimSetUpdate", df.format(lastSimSetUpdate)));
			sb.append(String.format("%1$s = %2$s%n", "bank", simset.getBankIp()));
			sb.append(String.format("%1$s = %2$s%n", "cardPos", simset.getCardPos()));
//			sb.append(String.format("%1$s = %2$s%n", "Approoved card", c.getName()));		
			}
		
			
		return sb.toString();
	}
	
	
	@Override
	public Class<?> getClazz() {return ChannelState.class;	}
	
	public enum Status {
		Ready, Failed, Unreach, Inchange, PeerInchange, Smsfetch, UssdRec;
	}

}
