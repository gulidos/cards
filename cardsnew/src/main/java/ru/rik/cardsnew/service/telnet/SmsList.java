package ru.rik.cardsnew.service.telnet;

import java.util.ArrayList;

import ru.rik.cardsnew.domain.MyState;
import ru.rik.cardsnew.domain.Sms;

public class SmsList implements MyState {
	private final long id;
	private final String name;
	private final ArrayList<Sms> smses;
	
	public SmsList(long id, String name) {
		super();
		this.id = id;
		this.name = name;
		this.smses = new ArrayList<>();
	}

	
	@Override public long getId() {return id;	}
	@Override public void setId(long id) {}

	@Override public String getName() {return name;}
	@Override public void setName(String name) {}

	@Override public Class<?> getClazz() {return SmsList.class;}
	

}
