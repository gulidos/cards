package ru.rik.cardsnew.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(of={"id", "name"})
public class BankState implements State {
	private long id;
	private String name;
	@Getter @Setter private boolean available = true;
	
	public BankState() {}

	@Override public long getId() {return id;	}
	@Override public void setId(long id) { this.id = id;	}

	@Override public String getName() {return name;}
	@Override public void setName(String name) {this.name = name;}
	@Override public Class<?> getClazz() {return BankState.class;	}
	
}
