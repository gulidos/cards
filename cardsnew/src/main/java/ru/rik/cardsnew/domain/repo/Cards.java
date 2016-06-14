package ru.rik.cardsnew.domain.repo;

import ru.rik.cardsnew.db.GenericRepo;
import ru.rik.cardsnew.domain.Card;

public class Cards extends GenericMemImpl<Card> {

	public Cards(GenericRepo<Card, Long> repo) {
		super(Card.class, repo);
	}
}
