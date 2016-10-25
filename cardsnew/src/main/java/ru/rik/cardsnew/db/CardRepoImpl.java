package ru.rik.cardsnew.db;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.CardStat;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Place;

@Repository
public class CardRepoImpl extends GenericRepoImpl<Card, CardStat> implements CardRepo  {
	static final Logger logger = LoggerFactory.getLogger(CardRepoImpl.class);
	private static final long serialVersionUID = 1L;
	private static CardRepoImpl repo;

	
	public CardRepoImpl() {
		super(Card.class, CardStat.class);
		System.out.println("!!! Create CardRepoImpl ");
	}
	
	@PostConstruct
	@Override
	public void Init() {
		logger.debug("post constructor initialisation {} repo", entityClass.getName());
		this.cb = em.getCriteriaBuilder();
		repo = this;
		for (Card c : findAll()) {
			CardStat s = addStateIfAbsent(c);
			if (c.getChannelId() != 0)
				s.setFree(true, false);
		}	
	}
	
	public static CardRepoImpl get() {return repo;	}

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
			if (c.getStat().isFree())
				result.add(c);
		return result;
    }

	@Override
	public Card findCardsByPlace(Place place, Bank bank) {
		return em.createNamedQuery("findCardByPlace", Card.class)
				.setParameter("place", place)
				.setParameter("bank", bank)
				.setHint("org.hibernate.cacheable", true)
				.getSingleResult();
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
	
}
