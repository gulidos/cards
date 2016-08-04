package ru.rik.cardsnew.db;

import java.util.List;

import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Grp;

public interface CardRepo extends GenericRepo<Card, Long>{
	public List<Card> findGroupCards(Grp grp);
}
