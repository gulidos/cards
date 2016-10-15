package ru.rik.cardsnew.http;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.rik.cardsnew.ConfigJpaLite;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.service.http.SimSet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigJpaLite.class)

public class TestSimSet {
	@PersistenceContext	protected EntityManager em;

	public TestSimSet() {}

	@Test
	@Transactional
	public void switchCard() throws IOException {
		TypedQuery<Channel> q = em.createQuery("SELECT c FROM Channel c WHERE c.name = :name", Channel.class)
				.setParameter("name","mgf92" );
		Channel ch = q.getSingleResult();
		
		TypedQuery<Card> qcard = em.createQuery("SELECT c FROM Card c WHERE c.name = :name", Card.class)
				.setParameter("name","mgf102" );
		Card c = qcard.getSingleResult();
		SimSet.post(ch, c);
		
		System.out.println(ch.toString());
		System.out.println(c.toString());
				
	}
	
//	@Test
	@Transactional
	public void t1changeCard() {

		TypedQuery<Channel> q = em.createQuery("SELECT c FROM Channel c", Channel.class)
				.setHint("org.hibernate.cacheable", true);
		Set<Channel> simSetJobs = new HashSet<>();
		
		for (Channel ch : q.getResultList()) {
			if (simSetJobs.contains(ch)) {
				simSetJobs.remove(ch);
				continue;
			}	
			try {
				Channel pair = ch.getPair();
				simSetJobs.add(pair);
				SimSet s = SimSet.get(ch, pair);
				
				SimSet pairS = s.getPairData();
				
				System.out.println("!!! me: " + s.getCh().getName() + " " + s.toString());
				if(pair != null)
					System.out.println("!!! Pair: " + pairS.getCh().getName() + " " + pairS.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("set: " + simSetJobs.size());
	}	
}
