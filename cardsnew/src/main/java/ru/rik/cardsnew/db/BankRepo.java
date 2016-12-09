package ru.rik.cardsnew.db;

import java.util.List;

import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.BankState;
import ru.rik.cardsnew.domain.Card;

public interface BankRepo extends GenericRepo<Bank, BankState>{

	void init();


	List<Card> getCardsInUse(long id);

}