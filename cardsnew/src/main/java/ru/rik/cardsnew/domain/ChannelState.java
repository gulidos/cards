package ru.rik.cardsnew.domain;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.rik.cardsnew.service.http.GsmState;
@Data
@NoArgsConstructor
@EqualsAndHashCode(of={"id", "name"})
public class ChannelState implements State {
	private long id;
	private String name;
	private volatile GsmState status;
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
		setStatus(GsmState.Unknown);
	}
	
	@Override
	public long getId() {return id;}
	public GsmState getStatus() {return status;	}
	public void setStatus(GsmState status) {this.status = status;}

	public Date getLastUpdate() {return lastUpdate;	}
	public void setLastUpdate(Date lastUpdate) {this.lastUpdate = lastUpdate;	}
	public void incPriority() {priority++;	}
	@Override public String getName() {return name;}
	@Override public void setName(String name) {this.name = name;}


	
}
