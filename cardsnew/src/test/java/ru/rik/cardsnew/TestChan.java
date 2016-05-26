package ru.rik.cardsnew;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.rik.cardsnew.db.JpaConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=JpaConfig.class)
public class TestChan {
	@Autowired
	EntityManagerFactory emf;
	public TestChan() {	}

	
	@Test
    public void clearData() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        clearAll(em, "from Channel c");
        clearAll(em, "from Trunk t");
        clearAll(em, "from Group g");
        clearAll(em, "from Box b");
//        clearAll(em, "from Publisher p");
        em.getTransaction().commit();
        em.close();
    }
	
	 private void clearAll(EntityManager em, String constraint) {
	        Query query = em.createQuery("delete " + constraint);
	        query.executeUpdate();
	    }
}
