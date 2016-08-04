package ru.rik.cardsnew.db;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Grp;

@Repository
public class CardRepoImpl extends GenericRepoImpl<Card, Long> implements CardRepo {
	private static final long serialVersionUID = 1L;

	
	public CardRepoImpl() {
		super(Card.class);
		System.out.println("!!! Create CardRepoImpl ");
	}
	
	@Override
    public List<Card> findGroupCards(Grp grp) {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Card> criteria = cb.createQuery(Card.class);

    	Root<Card> card = criteria.from(Card.class);
		TypedQuery<Card> query = em
				.createQuery(
						criteria.select(card).where(cb.equal(card.get("group"), cb.parameter(Grp.class, "group"))))
				.setParameter("group", grp)
				.setHint("org.hibernate.cacheable", true);

		return query.getResultList();
    }
	
	
}
