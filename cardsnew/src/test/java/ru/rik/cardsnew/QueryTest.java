package ru.rik.cardsnew;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.db.CardRepoImpl;
import ru.rik.cardsnew.domain.Card;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QueryTest {

	@Autowired
	CardRepoImpl cardRepo;
	@PersistenceContext
	protected EntityManager em;

	public QueryTest() {
	}

	@Test
	@Transactional
	@Rollback(false)
	public void t10loadData() {
		System.out.println("=========================Query==============");

		Query q = em.createQuery("SELECT i FROM Card i");
		List<Card> cards = q.getResultList();
		for (Card c : cards)
			System.out.println(c.toStringAll());

		TypedQuery<Card> tq = em.createQuery("select i from Card i where i.id = :id", Card.class).setParameter("id",
				2L);
		Card c = tq.getSingleResult();
		System.out.println(c.toStringAll());

		System.out.println("=========================CriteriaBuilder==============");

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Card> criteria = cb.createQuery(Card.class);
		Root<Card> card = criteria.from(Card.class);

		TypedQuery<Card> query = em
				.createQuery(
						criteria.select(card).where(cb.equal(card.get("name"), cb.parameter(String.class, "name"))))
				.setParameter("name", "ca2");

		List<Card> result = query.getResultList();
		for (Card crd : result)
			System.out.println(crd.toStringAll());
		
		TypedQuery<Card> query1 = em.createQuery(
				criteria.select(card).where(
						cb.in(card.get("name")).value("ca1").value("ca2").not()
				)
		);
	
		result = query1.getResultList();
		for (Card crd : result)
			System.out.println(crd.toStringAll());
	}

}
