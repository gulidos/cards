package ru.rik.cardsnew;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.rik.cardsnew.db.JpaConfig;
import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Oper;
import ru.rik.cardsnew.domain.Place;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=JpaConfig.class)
public class Test1 {
	
	@Autowired
	EntityManagerFactory emf;
	public Test1() {	}
    
	@Test
    public void clearData() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        clearAll(em, "from Card c");
        clearAll(em, "from Oper o");
        clearAll(em, "from Trunk t");
        clearAll(em, "from Bank b");
//        clearAll(em, "from Publisher p");
        em.getTransaction().commit();
        em.close();
    }
	

    private void clearAll(EntityManager em, String constraint) {
        Query query = em.createQuery("delete " + constraint);
        query.executeUpdate();
    }

	
	@Test
	public void test1() {
		 EntityManager em = emf.createEntityManager();
		 em.getTransaction().begin();
		 
		 Oper o1 = new Oper();
		 o1.setName("MTS");		 
		 
		 
		 Bank b1 = new Bank();
		 b1.setIp("10.10.10.1");
		 b1.setLocation("Somewhere out there");
		 
		 Card c1 = new Card();
		 c1.setName("mts737");
		 c1.setNumber("7777");
		 c1.setPlace(Place.b0000009);
		 c1.setSernumber("55555555555");
		 c1.setOper(o1);
	
		 c1.setBank(b1);
		 
		 Card c2 = new Card();
		 c2.setName("mts444");
		 c2.setPlace(Place.b0000007);
		 c2.setSernumber("22222");
		 c2.setOper(o1);
	
		 c2.setBank(b1);
		 
		 assertNotNull(o1);
		 o1.getCards().add(c1);
		 o1.getCards().add(c2);
		 
		 em.persist(o1);
//		 em.persist(c1);
//		 em.persist(c2);
		 em.getTransaction().commit();
	     em.close();
//	     	    
//	     em = emf.createEntityManager();
//		 em.getTransaction().begin();
//		 Card c3 = em.find(Card.class, c2.getId());
//	     em.remove(c3);
//	     System.out.println("================================================");
//	     em.getTransaction().commit();
//	     em.close();
//	     
	    
	     em = emf.createEntityManager();
		 em.getTransaction().begin();
		 Bank b = em.find(Bank.class, b1.getId());
	     System.out.println("================================================");
	     System.out.println("Bank : " + b.toString());
	     em.getTransaction().commit();
	     em.close();
		 
	}
	
	@Test
	public void test2() {
		 EntityManager em = emf.createEntityManager();
	        em.getTransaction().begin();
	        @SuppressWarnings("unchecked")
			List<Card> cards =  (List<Card>) em.createQuery("select s from Card s").getResultList();
	        for (Card card: cards) {
	        	System.out.println(card.toString());
	        }
	        
	        @SuppressWarnings("unchecked")
	        List<Oper> opers =  (List<Oper>) em.createQuery("select s from Oper s").getResultList();
	        for (Oper oper: opers) {
	        	System.out.println(oper.toString());
	        }
	        
	        em.getTransaction().commit();
	        em.close();
	}
	
	
	

}
