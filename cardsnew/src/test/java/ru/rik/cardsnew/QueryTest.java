package ru.rik.cardsnew;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Grp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigJpaLite.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QueryTest {

//	@Autowired	CardRepoImpl cardRepo;
	@PersistenceContext	protected EntityManager em;

	public QueryTest() {
	}

	@Test
	@Transactional
	@Rollback(false)
	public void t10loadData() {
		System.out.println("=========================Query==============");
		TypedQuery<Grp> groupQ = em.createQuery("SELECT g FROM Grp g WHERE g.id = :id", Grp.class)
				.setParameter("id", 2L);
		Grp g = groupQ.getSingleResult();
		System.out.println(g.toString());
		
		
		TypedQuery<Card> q = em.createQuery("SELECT i FROM Card i WHERE i.group = :g"
				+ " and i.active = true and i.channelId <> 0" , Card.class)
				.setParameter("g", g)
				.setHint("org.hibernate.cacheable", true);
		List<Card> cards = q.getResultList();
		for (Card c : cards)
			System.out.println(c.toStringAll());

		
	}

	
	
}
