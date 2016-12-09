package ru.rik.cardsnew.db;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.BankState;
import ru.rik.cardsnew.domain.Card;

@Repository
public class BankRepoImpl extends GenericRepoImpl<Bank, BankState> implements BankRepo  {
	private static final long serialVersionUID = 1L;
	


	public BankRepoImpl() {
		super(Bank.class, BankState.class);		
	}
	@PostConstruct
	@Override
	public void init() {
		super.init();
		logger.debug("init");
	}
	
	@Override @Transactional
	public List<Card> getCardsInUse(long id ){
		Bank b = findById(id);
		return b.getCards().stream()
			.filter(c-> !c.isBlocked())
			.filter(c -> c.getChannelId() != 0)
			.collect(Collectors.toList());
	}
//	public Bank findBankByIp(String ip) {
//		
//	}

}
