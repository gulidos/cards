package ru.rik.cardsnew.db;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.web.CardForm;

@Repository
public class CardsRepositoryImpl implements CardsRepository {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long count() {
		return findAll().size();
	}

	@Transactional
	@Override
	public Card save(Card card) {
		entityManager.persist(card);
		return card;
	}
	
	@Transactional
	@Override
	public void update(CardForm c) {
		System.out.println("update card: " + c.toString());
		Card card = findOne(c.getId());
		card.setCard(c);
	}

	@Transactional
	@Override
	public void delete(Card card) {
		Card c = findOne(card.getId());
		if (c != null)
		entityManager.remove(c);
	}

	@Override
	public Card findOne(long id) {
		return entityManager.find(Card.class, id);
	}

	@Override
	public Card findByUsername(String name) {
		return (Card) entityManager.createQuery("select s from Card s where s.name=?").setParameter(1, name)
				.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Card> findAll() {
		return (List<Card>) entityManager.createQuery("select s from Card s").getResultList();

	}

}
