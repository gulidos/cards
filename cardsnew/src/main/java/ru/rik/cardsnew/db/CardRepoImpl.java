package ru.rik.cardsnew.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.domain.Balance;
import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.CardStat;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Limit;
import ru.rik.cardsnew.domain.Place;
import ru.rik.cardsnew.domain.Sms;

@Repository
public class CardRepoImpl extends GenericRepoImpl<Card, CardStat> implements CardRepo  {
	static final Logger logger = LoggerFactory.getLogger(CardRepoImpl.class);
	private static final long serialVersionUID = 1L;
	@Autowired BankRepo banks;
	
	public CardRepoImpl() {
		super(Card.class, CardStat.class);
		System.out.println("!!! Create CardRepoImpl ");
	}
	
	@PostConstruct
	@Override
	public void init() {
		super.init();
		logger.debug("post constructor initialisation {} repo", entityClass.getName());
		for (Card c : findAll()) {
			CardStat s = addStateIfAbsent(c);
			if (c.getChannelId() != 0)
				s.setFree(true, false);
		}
		
		findLastBalances().forEach(b-> {
			CardStat st = findStateById(b.getCard().getId());
			if (st != null)
				st.applyBalance(b);
			});
	}

	
	@Override
	public List<Card> findGroupCards(Grp grp) {
		return em.createNamedQuery("findAllCardsInGrp", Card.class)
				.setParameter("g", grp)
				.setHint("org.hibernate.cacheable", true)
				.getResultList();
    }
	
	@Override
	public List<Card> findFreeCardsInGroup(Grp grp) {
		List<Card> result = new ArrayList<>();
		for (Card c : em.createNamedQuery("findActiveCardsInGrp", Card.class).setParameter("g", grp)
				.setHint("org.hibernate.cacheable", true).getResultList())
			if (c.getStat(this).isFree())
				result.add(c);
		return result;
    }

	@Override
	public Card findCardsByPlace(Place place, Bank bank) {
		try {
			return em.createNamedQuery("findCardByPlace", Card.class)
					.setParameter("place", place)
					.setParameter("bank", bank)
					.setHint("org.hibernate.cacheable", true)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
    }

	@Override
	public List<Card> findBankCards(Bank bank) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public CardStat addStateIfAbsent(Card entity) {
		CardStat cs= super.addStateIfAbsent(entity);
		cs.setRepo(this);
		return cs;
	}
	
	@Override
	public List<Card> findAllAvailableForChannel(Grp g) {
		return g.getCards().stream()
		.filter(c->isEligible(c))
		.sorted((c1, c2) -> Long.compare(c1.getId(), c2.getId()))
		.sorted((c1, c2) -> Integer.compare(c2.getStat(this).getMinRemains(), c1.getStat(this).getMinRemains()))
		.collect(Collectors.toList());
	}

	
	@Override
	public Card findTheBestInGroupForInsert(Grp g) {
		Optional<Card> oc = g.getCards().stream()
		.filter(c->isEligible(c))
		.peek(c -> System.out.println(c.getName() + " " + c.getStat(this).getMinRemains()))
		.sorted((c1, c2) -> Long.compare(c1.getId(), c2.getId()))
		.sorted((c1, c2) -> Integer.compare(c2.getStat(this).getMinRemains(), c1.getStat(this).getMinRemains()))
		.findFirst();
		if (oc.isPresent())
			return oc.get();
		else return null;
	}
	
	private boolean isEligible(Card c) {
		return c.isActive() && !c.isBlocked()
				&& c.getStat(this).isFree() 
				&& c.getBank().getStat(banks).isAvailable() 
				&& c.getStat(this).getMinRemains() > 1;
	}
	
	@Override
	public List<Card> findAll() {
		return super.findAll().stream()
				.filter(c-> !c.isBlocked())
				.collect(Collectors.toList());

	}
	
	@Override
	public List<Card> findAllAndBlocked() {
		return super.findAll();

	}
	
	@Override
	public List<Limit> getLimits() {
		return em.createNamedQuery("findAllLimits", Limit.class)
				.setHint("org.hibernate.cacheable", true)
				.getResultList();
	}
	
	@Override @Transactional
	public void refreshLimits() {
		findAll().stream()
		.peek(c-> c.refreshDayLimit())
		.forEach(c-> makePersistent(c));
//		for (Card c: findAll()) {
//			c.refreshDayLimit();
//			makePersistent(c);
//		}	
	}
	

	
	@Override @Transactional
	public void setChannelToNull(List<Card> list) {
		list.stream()
		.filter(c -> c.getChannelId() != 0)
		.peek(c -> c.setChannelId(0)).peek(c->makePersistent(c))
		.map(c -> c.getStat(this))
		.forEach(st -> st.setFree(false, true));
	}

	
	@Override
	public List<Balance> findLastBalances() {
		return em.createNamedQuery("findAllLastBalance", Balance.class)
				.setHint("org.hibernate.cacheable", true)
				.getResultList();
	}

	@Override
	public void updateDayLimit() {
		// TODO Auto-generated method stub
		
	}
	
	@Override @Transactional
	public void balanceSave(Balance b) {
			em.merge(b);
	}
}
