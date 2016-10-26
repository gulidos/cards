package ru.rik.cardsnew.db;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.BankState;

@Repository
public class BankRepoImpl extends GenericRepoImpl<Bank, BankState> implements BankRepo  {
	private static final long serialVersionUID = 1L;
	private static BankRepoImpl repo;


	public BankRepoImpl() {
		super(Bank.class, BankState.class);		
	}
	@PostConstruct
	@Override
	public void init() {
		super.init();
		logger.debug("init");
		repo = this;
	}
	
	public static BankRepoImpl get() {return repo;	}
	public static void setRepo(BankRepoImpl repo) {	BankRepoImpl.repo = repo;}

}
