package ru.rik.cardsnew.db;

import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Bank;

@Repository
public class BankRepoImpl extends GenericRepoImpl<Bank, Long>  {
	private static final long serialVersionUID = 1L;


	public BankRepoImpl() {
		super(Bank.class);		
	}
}
