package ru.rik.cardsnew.db;

import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.BankState;

@Repository
public class BankRepoImpl extends GenericRepoImpl<Bank, BankState>  {
	private static final long serialVersionUID = 1L;


	public BankRepoImpl() {
		super(Bank.class, BankState.class);		
	}
}
