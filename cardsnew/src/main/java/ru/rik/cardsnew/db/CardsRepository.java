package ru.rik.cardsnew.db;

import java.util.List;

import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.web.CardForm;

public interface CardsRepository {
	 long count();

	  Card save(Card spitter);

	  Card findOne(long id);

	  public void update(CardForm card);
	  
	  Card findByUsername(String username);

	  List<Card> findAll();

	public void delete(Card card);

}
