package ru.rik.cardsnew.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.CardStat;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Limit;
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
	public void init() {
		super.init();
		logger.debug("post constructor initialisation {} repo", entityClass.getName());
		repo = this;
		for (Card c : findAll()) {
			CardStat s = addStateIfAbsent(c);
			if (c.getChannelId() != 0)
				s.setFree(true, false);
		}	
	}
	
	public static CardRepoImpl get() {return repo;	}
	public static void set(CardRepo r) {repo = (CardRepoImpl) r;}
	
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
		.filter(c->c.getStat().isFree() 
				&& c.isActive()  
				&& c.getBank().getStat().isAvailable() 
				&& c.getStat().getMinRemains() > 1)
		.sorted((c1, c2) -> Long.compare(c1.getId(), c2.getId()))
		.sorted((c1, c2) -> Integer.compare(c2.getStat().getMinRemains(), c1.getStat().getMinRemains()))
		.collect(Collectors.toList());
	}
	
	@Override
	public Card findTheBestInGroupForInsert(Grp g) {
		Optional<Card> oc = g.getCards().stream()
		.filter(c->c.getStat().isFree() 
				&& c.isActive()  
				&& c.getBank().getStat().isAvailable()  
				&& c.getStat().getMinRemains() > 1)
		.sorted((c1, c2) -> Long.compare(c1.getId(), c2.getId()))
		.sorted((c1, c2) -> Integer.compare(c2.getStat().getMinRemains(), c1.getStat().getMinRemains()))
		.findFirst();
		if (oc.isPresent())
			return oc.get();
		else return null;
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
	
	@Override
	public void updateDayLimit() {
		Random rnd = new Random();
		int range = rnd.nextInt();

		
	}
}
