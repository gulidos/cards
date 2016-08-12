package ru.rik.cardsnew.domain;

import java.util.Date;

import lombok.Data;
@Data
public class ChannelState {
	private final long channelId;
	private volatile ChStatus status;
	private volatile int order;
	private Date lastUpdate;

	
	public ChannelState(Channel t) {
		channelId = t.getId();
		lastUpdate = new Date();
		setStatus(ChStatus.Unknown);
	}

	public long getTrunkId() {return channelId;}
	public ChStatus getStatus() {return status;	}
	public void setStatus(ChStatus status) {this.status = status;}

	public Date getLastUpdate() {return lastUpdate;	}
	public void setLastUpdate(Date lastUpdate) {this.lastUpdate = lastUpdate;	}


	
	public enum ChStatus {
		Unknown,
		Booting,
		Initing,
		Listening, 
		Standby,
		Ending, 
		Unreachable; 
		
		public static ChStatus getInstance(String code) {
		switch (code) {
		case "Standby":
			return Standby;
		case "Listening":
			return Listening;
		case "Booting":
			return Booting;
		case "Initing":
			return Initing;
		case "Ending":
			return Ending;	
		default:
			return Unknown;
		}	
	}
		
	}
	
}
