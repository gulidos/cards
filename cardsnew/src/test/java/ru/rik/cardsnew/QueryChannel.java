package ru.rik.cardsnew;

import java.util.List;
import java.util.Set;

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
import ru.rik.cardsnew.db.JpaConfig;
import ru.rik.cardsnew.db.TrunkRepoImpl;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Trunk;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JpaConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QueryChannel {

	@Autowired	CardRepoImpl cardRepo;
	@Autowired	ChannelRepoImpl chanRepo;
	@Autowired	TrunkRepoImpl trunks;
	@PersistenceContext
	protected EntityManager em;

	public QueryChannel() {
	}

	@Test
	@Transactional
	@Rollback(false)
	public void t10loadFist() {
		System.out.println("=========================t10loadFist==============");
		Trunk t = trunks.findById(1);
		Set<Channel> list = t.getChannels();
		for (Channel ch: list )
			System.out.println("channel: " + ch.getName());
		
	
		
	}

//	@Test
	@Transactional
	@Rollback(false)
	public void t20loadFist() {
		System.out.println("=========================t20loadFist==============");
		Channel ch = chanRepo.findById(1L);
		System.out.println("2 First channel: " + ch.getName());
		System.out.println("2 Second channel: " + chanRepo.findPair(ch).getName());
	}
}
