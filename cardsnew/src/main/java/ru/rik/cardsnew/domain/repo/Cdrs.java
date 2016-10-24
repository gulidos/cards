package ru.rik.cardsnew.domain.repo;

import java.util.Deque;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.log4j.Logger;

import ru.rik.cardsnew.domain.events.Cdr;

public class Cdrs {
	static final Logger logger = Logger.getLogger(Cdrs.class);
	private final Deque<Cdr> cdrsList;
	private final NavigableMap<String, Cdr> cdrsByCard;
	
	
	public Cdrs() {
		this.cdrsList = new ConcurrentLinkedDeque<>();
		this.cdrsByCard = new ConcurrentSkipListMap<>();
	}
	
	public boolean  addCdr(Cdr cdr) {
		cdrsByCard.put(cdr.getCardId() + "_" + cdr.getUniqueid(), cdr);
		return cdrsList.offerFirst(cdr);
	}
	
	public SortedMap <String, Cdr> findCdrByCards(long id) {
		String keyFrom = String.valueOf(id) + "_" + "0";
		String keyTo = String.valueOf(id) + "_" + "A";
		return cdrsByCard.subMap(keyFrom, keyTo);
	}

}
