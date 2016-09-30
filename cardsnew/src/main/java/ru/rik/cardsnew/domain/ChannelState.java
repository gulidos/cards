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
	
	private volatile GsmState gsmstatus;
	private Date lastUpdate;
	
	private volatile int priority;
	
	public ChannelState(Channel t) {
		id = t.getId();
		lastUpdate = new Date();
	}
	
	public void incPriority() {priority++;	}	
}
