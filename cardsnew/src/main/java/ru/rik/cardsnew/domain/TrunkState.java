package ru.rik.cardsnew.domain;

import lombok.Data;

@Data
public class TrunkState {
	private long id;
	private int last;

	
	public TrunkState(Trunk t) {
		id = t.getId();
		last = 1;
	}
	
	

}
