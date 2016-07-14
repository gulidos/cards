package ru.rik.cardsnew.domain;

import java.util.concurrent.atomic.AtomicInteger;

public class TrunkState {
	private final long trunkId;
	private final AtomicInteger next;

	
	public TrunkState(Trunk t) {
		trunkId = t.getId();
		next = new AtomicInteger(0);
	}


	public int getNext() {
		return next.getAndIncrement();
	}
	
	public void setNext(int i) {
		next.set(i);
	}

	public long getTrunkId() {return trunkId;}
	
}
