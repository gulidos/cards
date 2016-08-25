package ru.rik.cardsnew.domain.repo;

import java.util.Deque;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import ru.rik.cardsnew.db.CardRepoImpl;
import ru.rik.cardsnew.domain.CardStat;
import ru.rik.cardsnew.domain.events.Cdr;

public class Cdrs {
	static final Logger logger = Logger.getLogger(Cdrs.class);
	private final Deque<Cdr> cdrsList;
	private final NavigableMap<String, Cdr> cdrsByCard;
	
	@Autowired CardRepoImpl cardRepo;
	
	public Cdrs() {
		this.cdrsList = new ConcurrentLinkedDeque<>();
		this.cdrsByCard = new ConcurrentSkipListMap<>();
	}
	
	public boolean  addCdr(Cdr cdr) {
		

//		cdrsByCard.put(cdr.ge, value)
		return cdrsList.offerFirst(cdr);
	}

}
