package ru.rik.cardsnew;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.rik.cardsnew.db.CardRepoImpl;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.db.TrunkRepoImpl;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Trunk;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestChangeChansInTrunk {
//	@Autowired GroupRepoImpl groups;
//	@Autowired BoxRepoImpl boxes;
	@Autowired TrunkRepoImpl trunks;
	@Autowired ChannelRepo chans;
	@Autowired CardRepoImpl cards;

	public TestChangeChansInTrunk() {
	}

	public static Set<Trunk> oldTrunks;
	public static long trunkId = 2;
	public static long chanId = 1;
	
	@Test
	@Transactional
	@Rollback(false)
	public void t1changeCard() {
		System.out.println("=========================t1changeCard==============");
		Trunk t = trunks.findById(trunkId);
		System.out.println("================ Trunk before: " + t.toStringAll());
		
		Channel ch = chans.findById(chanId);
		oldTrunks = ch.getTrunks();
		System.out.println("================ Channel before: " + ch.toString());
		
		Set<Trunk> newTrunks = new HashSet<>(); 
		newTrunks.add(t);
		ch.setTrunks(newTrunks);
//		
		Set<Channel> newChannels = new HashSet<>();
		newChannels.add(ch);
		t.setChannels(newChannels);
		//FIXME needs to be done carefully
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void t2check() {
		System.out.println("=========================t2check==============");
		Channel ch = chans.findById(chanId);
		System.out.println("================ Channel after: " + ch.toString());
		
		
		Trunk t = trunks.findById(trunkId);
		System.out.println("================ Trunk after: " + t.toStringAll());
		System.out.println("================ Old trunks  after: " + oldTrunks.toString());
	}	
	
	@Test
	@Transactional
	@Rollback(false)
	public void t3back() {
		System.out.println("=========================t2back==============");

		Channel ch = chans.findById(chanId);
		ch.setTrunks(oldTrunks);
		chans.makePersistent(ch);
		System.out.println("Channel after back: " + ch.toString());

	}
	
}
