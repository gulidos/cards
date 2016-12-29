package ru.rik.cardsnew.db;

import java.util.List;

import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.CardStat;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Limit;
import ru.rik.cardsnew.domain.Place;
import ru.rik.cardsnew.domain.Ussd;

public interface CardRepo extends GenericRepo<Card, CardStat> {

	List<Card> findGroupCards(Grp grp);

	List<Card> findFreeCardsInGroup(Grp grp);
	List<Card> findBankCards(Bank bank);

	Card findCardsByPlace(Place place, Bank bank);

	List<Card> findAllAvailableForChannel(Grp g);

	Card findTheBestInGroupForInsert(Grp g);

	List<Card> findAllAndBlocked();

	void updateDayLimit();

	List<Limit> getLimits();

	void refreshLimits();

	void init();

	void setChannelToNull(List<Card> list);

	List<Ussd> findLastBalances();

//	void balanceSave(Balance b);

}