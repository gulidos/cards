package ru.rik.cardsnew.domain;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.asteriskjava.manager.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.rik.cardsnew.config.Settings;
import ru.rik.cardsnew.db.BankRepo;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.service.asterisk.AsteriskEvents;
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
	private volatile Date lastGsmUpdate = new Date();
	private volatile Date nextGsmUpdate = new Date(); 
	
	private volatile SimSet simset;
	private volatile Date lastSimSetUpdate = new Date();
	private volatile Date nextSimSetUpdate = new Date();
	
	@Getter private volatile Status status = Status.Failed;
	private volatile Date lastStatusChange = new Date();
	
	@Getter private volatile Date lastSmsFetchDate = new Date(0);
	@Getter private volatile Date nextSmsFetchDate = new Date(0);
	
	@Getter private volatile Date lastBalanceCheck = new Date(0);
	@Getter private volatile Date nextBalanceCheck = new Date(0);
	
	private final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

	public ChannelState () {
	}
	
	public ChannelState (Status s, Date lastStatusChange ) {
		this.status = s;
		this.lastStatusChange = lastStatusChange;
	}
	
	public void applyGsmStatus(GsmState gs) {
		if (gs.isReady()) {
			nextGsmUpdate = Util.getNowPlusSec(Settings.NORM_INTERVAL);
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
		nextSimSetUpdate = Util.getNowPlusSec(Settings.NORM_INTERVAL);
		lastSimSetUpdate = new Date();
	}
	
	
	public void setStatus(Status s) {
		switch (s) {
		case Failed:	setFailedStatus(s);break;
		case AwaitForPeer:
		case Ready:		setReadyStatus(s); break;
		case Unreach:	setUnreachStatus(s); break;
		case Inchange: 
		case PeerInchange: setChangeStatus(s); break;
		case Smsfetch: 
			lastSmsFetchDate = new Date(); 
			setMantainStatus(s); break;
		case UssdReq:	setMantainStatus(s); break;
		default:
			throw new IllegalArgumentException("Unknown channel's status: " + s.toString());
		}
	}

	private void setFailedStatus(Status s) {
		switch (status) {
		case Inchange:	case PeerInchange:
			if (!isStillHasToBeIniting()) {
				nextGsmUpdate = Util.getNowPlusSec(Settings.NORM_INTERVAL);					
				this.status = s; //Become Failed
				lastStatusChange = new Date();
			} else 
				nextGsmUpdate = Util.getNowPlusSec(Settings.FAIL_INTERVAL);
			break;
		case Failed:
			if (isStillHasToBeIniting())
				nextGsmUpdate = Util.getNowPlusSec(Settings.FAIL_INTERVAL);
			else {
				nextGsmUpdate = Util.getNowPlusSec(Settings.NORM_INTERVAL);
				logger.info("channel {} is failed", getName());
			}	
			break;
		case Ready:	case UssdReq: case Smsfetch: case Unreach:	 // has been Unreachable, become Failed
			this.status = s; //Become Failed
			lastStatusChange = new Date();
			nextSimSetUpdate = Util.getNowPlusSec(Settings.FAIL_INTERVAL);
			break;
		default:
			break;
		}
	}
	
	private void setReadyStatus(Status s) {
		if (status == Status.AwaitForPeer) {
			nextGsmUpdate = Util.getNowPlusSec(Settings.NORM_INTERVAL);
			return;
		}	
		if (s != this.status) {
			lastStatusChange = new Date();
			if (this.status == Status.Failed || this.status == Status.Unreach)
				logger.info("channel {} is available", getName());
		}
		nextGsmUpdate = Util.getNowPlusSec(Settings.NORM_INTERVAL);
		this.status = s;
		
	}
	
	
	private void setChangeStatus(Status s) {
		if (s != this.status)
			lastStatusChange = new Date();
		this.status = s;
		nextGsmUpdate = Util.getNowPlusSec(Settings.FAIL_INTERVAL);
	}
	
	private void setUnreachStatus(Status s) {
		if (status == Status.Unreach || status == Status.Failed) {
			if(isStillHasToBeIniting()) 
				nextGsmUpdate = Util.getNowPlusSec(Settings.FAIL_INTERVAL);
			else {
				nextGsmUpdate = Util.getNowPlusSec(Settings.NORM_INTERVAL);
				logger.info("channel {} is unreachable", getName());
			}	
		} else if ( status == Status.Inchange || status == Status.PeerInchange) {
			if(isStillHasToBeIniting()) 
				nextGsmUpdate = Util.getNowPlusSec(Settings.FAIL_INTERVAL);
			else {
				logger.info("channel {} is unreachable", getName());
				nextGsmUpdate = Util.getNowPlusSec(Settings.NORM_INTERVAL);
				this.status = s;
				lastStatusChange = new Date();
			}
		} else {
			logger.info("channel {} is unreachable", getName());
			lastStatusChange = new Date();
			this.status = s;
			nextGsmUpdate = Util.getNowPlusSec(Settings.FAIL_INTERVAL);
		}
	}
	
	private void setMantainStatus(Status s) {
		if (status == Status.Ready) {
			this.status = s;
			lastStatusChange = new Date();
			nextGsmUpdate = Util.getNowPlusSec(Settings.NORM_INTERVAL);
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
	
	public boolean isSmsFetchDateFresh() {
		return isDateFresh(nextSmsFetchDate);
	}
	
	private boolean isDateFresh(Date nextdate) {
		long now = new Date().getTime();
		return now - nextdate.getTime() < 0;
	}
	
	public boolean isInUse(AsteriskEvents astMngr) {
		try {
			String state = astMngr.getDeviceState(getName());
			if ("NOT_INUSE".equals(state))
				return false;
			else 
				return true;
		} catch (IllegalArgumentException | IllegalStateException | IOException | TimeoutException e) {
			logger.error(e.getMessage(), e);
			return true;
		}
	}
	
	
	public void incPriority() {priority.incrementAndGet();	}
	public int getPriority() {	return priority.get();	}
	
	public String toWeb(CardRepo cards, ChannelRepo chans, BankRepo banks, AsteriskEvents astMngr) {
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("%1$s = %2$s%n", "name", getName()));
		Card c = chans.findById(getId()).getCard();
		sb.append(String.format("%1$s = %2$s%n", "card", c != null ? c.getName() : "none"));
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
		
		Place place = simset != null ? Place.getInstance(simset.getCardPos()) : null;
		Bank bank = simset != null ? banks.findByName(simset.getBankIp()) : null;

		c = cards.findCardsByPlace(place, bank);
		
		if (simset != null) {
			sb.append(" \n");
			sb.append(String.format("%1$s = %2$s%n", "lastSimSetUpdate", df.format(lastSimSetUpdate)));
			sb.append(String.format("%1$s = %2$s%n", "bank", simset.getBankIp()));
			sb.append(String.format("%1$s = %2$s%n", "cardPos", simset.getCardPos()));
			sb.append(String.format("%1$s = %2$s%n", "Approoved card", 
					c != null ? c.getName() : "unknown"));		
			}
		
		try {
			sb.append(String.format("%1$s = %2$s%n", "Asterisk status", astMngr.getDeviceState(getName())));
		} catch (IllegalArgumentException | IllegalStateException | IOException | TimeoutException e) {
			logger.error(e.getMessage(), e);
		}
			
		return sb.toString();
	}
	
	
	@Override
	public Class<?> getClazz() {return ChannelState.class;	}
	
	public enum Status {
		Ready, Failed, Unreach, AwaitForPeer, Inchange, PeerInchange, Smsfetch, UssdReq;
	}

}
