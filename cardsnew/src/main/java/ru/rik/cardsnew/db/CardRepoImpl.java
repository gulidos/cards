package ru.rik.cardsnew.db;

import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Card;

@Repository
public class CardRepoImpl extends GenericRepoImpl<Card, Long> implements CardRepo {
	private static final long serialVersionUID = 1L;

	
	public CardRepoImpl() {
		super(Card.class);
		System.out.println("!!! Create CardRepoImpl ");
	}
	
}
