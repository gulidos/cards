package ru.rik.cardsnew.http;

import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.rik.cardsnew.ConfigJpaLite;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.service.http.SimSet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigJpaLite.class)

public class TestSimSet {
	@PersistenceContext	protected EntityManager em;

	public TestSimSet() {}

	@Test
	@Transactional
	public void t1changeCard() throws IOException {

		TypedQuery<Channel> q = em.createQuery("SELECT c FROM Channel c", Channel.class);
		
		
		for (Channel ch : q.getResultList()) {
			System.out.println("going for chanel " + ch.getName());
			SimSet s = SimSet.get(ch);
			System.out.println(s.toString());
		}
	}	
}
