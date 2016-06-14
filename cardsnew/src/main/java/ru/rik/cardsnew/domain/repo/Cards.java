package ru.rik.cardsnew.domain.repo;

import ru.rik.cardsnew.domain.Card;

public class Cards extends GenericMemImpl<Card> {

	public Cards() {
		super(Card.class);
	}
}
