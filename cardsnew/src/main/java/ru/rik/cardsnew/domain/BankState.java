package ru.rik.cardsnew.domain;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.rik.cardsnew.service.http.BankStatus;

@EqualsAndHashCode(of={"id", "name"})
public class BankState implements State {
	private static final Logger logger = LoggerFactory.getLogger(BankState.class);		

	private long id;
	private String name;
	@Getter @Setter private volatile BankStatus bankstatus;
	@Getter @Setter private volatile Date lastUpdate;
	@Getter @Setter private volatile Date nextUpdate;

	@Getter  private boolean available = true;
	@Getter @Setter private volatile Date lastStatusChange;
	
	public BankState() {
		available = true;
		this.lastStatusChange = new Date();
	}
	
	public BankState(Bank b) {
		id = b.getId();
		name = b.getName();
		available = true;
		this.lastStatusChange = new Date();
	}

	public void applyBankStatus(BankStatus s) {
		bankstatus = s;
		setAvailable(true);
		lastUpdate = new Date();
	}
	
	public void setAvailable(boolean a) {
		if (!this.available == a) {
			lastStatusChange = new Date();
			if (a == true) 
				logger.info("bank {} is avaliable", getName());
			else 
				logger.info("bank {} is Unavaliable", getName());
		}	
		available = a;
	}
	
	@Override public long getId() {return id;	}
	@Override public void setId(long id) { this.id = id;	}

	@Override public String getName() {return name;}
	@Override public void setName(String name) {this.name = name;}
	@Override public Class<?> getClazz() {return BankState.class;	}
	
}
