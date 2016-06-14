package ru.rik.cardsnew.domain.repo;

import ru.rik.cardsnew.db.GenericRepo;
import ru.rik.cardsnew.domain.Bank;
public class Banks extends GenericMemImpl<Bank> {
	
	public Banks(GenericRepo<Bank, Long> repo) {
		super(Bank.class, repo);
	}
}
