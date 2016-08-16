package ru.rik.cardsnew.domain.repo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ru.rik.cardsnew.domain.CardStat;

public class CardsStates  {
	ConcurrentMap<Long, CardStat> cards;
	
	public CardsStates() {	
		cards = new ConcurrentHashMap<>();
	}
	
	/**  Returns:	the previous value associated with the specified key, 
	 * or null if there was no mapping for the key*/
	public CardStat add(CardStat c) {
		return cards.putIfAbsent(c.getCardId(), c);
	}
	
	
	public CardStat findById (long id) {
		return cards.get(id);
	}
	
}
