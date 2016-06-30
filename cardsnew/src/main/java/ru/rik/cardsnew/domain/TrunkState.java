package ru.rik.cardsnew.domain;

import lombok.Data;

@Data
public class TrunkState {
	private long trunkId;
	private int next;

	
	public TrunkState(Trunk t) {
		trunkId = t.getId();
		next = 1;
	}
}
