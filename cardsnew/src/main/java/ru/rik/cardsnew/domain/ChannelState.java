package ru.rik.cardsnew.domain;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class ChannelState implements State {
	private long id;
	private String name;
	private volatile ChStatus status;
	private volatile int priority;
	private Date lastUpdate;
	private String operator;
	private String sernum;
	private int sigquality;
	private String regstate;
	private String iURL;
	private String iName;
	private String oMob;
	
	public ChannelState(Channel t) {
		id = t.getId();
		lastUpdate = new Date();
		setStatus(ChStatus.Unknown);
	}
	
	@Override
	public long getId() {return id;}
	public ChStatus getStatus() {return status;	}
	public void setStatus(ChStatus status) {this.status = status;}

	public Date getLastUpdate() {return lastUpdate;	}
	public void setLastUpdate(Date lastUpdate) {this.lastUpdate = lastUpdate;	}
	public void incPriority() {priority++;	}
	@Override public String getName() {return name;}
	@Override public void setName(String name) {this.name = name;}

	
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
