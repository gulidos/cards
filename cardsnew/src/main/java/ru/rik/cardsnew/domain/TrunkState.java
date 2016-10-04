package ru.rik.cardsnew.domain;

import java.util.concurrent.atomic.AtomicInteger;


public class TrunkState extends MyState {
	private long id;
	private String name;
	private AtomicInteger next;

	public TrunkState() {}
	
	public TrunkState(Trunk t) {
		id = t.getId();
		next = new AtomicInteger(0);
	}


	public int getNext() {
		return next.getAndIncrement();
	}
	
	public void setNext(int i) {
		next.set(i);
	}
	
	@Override public long getId() {return id;}
	@Override public void setId(long id) { this.id = id;}
	
	@Override public String getName() {return name;}
	@Override public void setName(String name) {this.name = name;}

	@Override
	public Class<?> getClazz() {	return TrunkState.class;	}
}
