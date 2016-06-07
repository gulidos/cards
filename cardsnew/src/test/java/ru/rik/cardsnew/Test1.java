package ru.rik.cardsnew;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.db.CardRepoImpl;
import ru.rik.cardsnew.db.GroupRepoImpl;
import ru.rik.cardsnew.db.JpaConfig;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Oper;
@EnableTransactionManagement
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=JpaConfig.class)
public class Test1 {
	
	@Autowired
	EntityManagerFactory emf;	
	
	@Autowired
	CardRepoImpl cardsRepository;
	@Autowired
	GroupRepoImpl grpRepo;
	public Test1() {}
    
	
	
	@Transactional
//    @Test
    @Rollback(false) // All tests by default rollback transactions
	public void insertData( ) {
		 Oper o1 = new Oper();
		 o1.setName("BBB");		 
		 
		 Grp g1 = new Grp();
		 g1.setName("3211231s");

		 grpRepo.makePersistent(g1);
	}
	
	
//    @Test
    @Transactional @Rollback(false)
	public void removeDate() {
		Grp g = grpRepo.findById(20L);
		if (g!= null)
			grpRepo.makeTransient(g);
	}
	
	@Transactional
    @Test
	public void test3() {
    	
    	List<Grp> grps =  grpRepo.findAll();
    	for (Grp grp: grps) {
        	System.out.println(grp.toString() + grp.getCards().toString() );
        }
    	
//    	List<Card> cards =  cardsRepository.findAll();
//    	for (Card card: cards) {
//        	System.out.println(card.toString() + " group: " + card.getGroup().getName() + " bank: " + card.getBank().getIp());
//        }
//    	
    }
	

	
//	@Test
    public void clearData() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        clearAll(em, "from Card c");
//        clearAll(em, "from Oper o");
        clearAll(em, "from Grp g");
//        clearAll(em, "from Publisher p");
        em.getTransaction().commit();
        em.close();
    }
	

    private void clearAll(EntityManager em, String constraint) {
        Query query = em.createQuery("delete " + constraint);
        query.executeUpdate();
    }

    
}
