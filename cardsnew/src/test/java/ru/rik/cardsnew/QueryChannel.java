package ru.rik.cardsnew;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import ru.rik.cardsnew.db.ChannelRepoImpl;
import ru.rik.cardsnew.domain.Channel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QueryChannel {

	@Autowired	CardRepoImpl cardRepo;
	@Autowired	ChannelRepoImpl chanRepo;
	@PersistenceContext
	protected EntityManager em;

	public QueryChannel() {
	}

	@Test
	@Transactional
	@Rollback(false)
	public void t10loadFist() {
		System.out.println("=========================t10loadFist==============");
		Channel ch = chanRepo.findById(1L);
		System.out.println("1 First channel: " + ch.getName());
		System.out.println("1 Second channel: " + chanRepo.findPair(ch).getName());
	
		
	}

	@Test
	@Transactional
	@Rollback(false)
	public void t20loadFist() {
		System.out.println("=========================t20loadFist==============");
		Channel ch = chanRepo.findById(1L);
		System.out.println("2 First channel: " + ch.getName());
		System.out.println("2 Second channel: " + chanRepo.findPair(ch).getName());
	}
}
