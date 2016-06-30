package ru.rik.cardsnew;

import java.util.HashSet;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.db.BankRepoImpl;
import ru.rik.cardsnew.db.BoxRepoImpl;
import ru.rik.cardsnew.db.CardRepoImpl;
import ru.rik.cardsnew.db.ChannelRepoImpl;
import ru.rik.cardsnew.db.GroupRepoImpl;
import ru.rik.cardsnew.db.JpaConfig;
import ru.rik.cardsnew.db.TrunkRepoImpl;
import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Oper;
import ru.rik.cardsnew.domain.Place;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=JpaConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoadData {
	@Autowired
	CardRepoImpl cardRepo;
	@Autowired
	BankRepoImpl bankRepo;
	@Autowired
	GroupRepoImpl grpRepo;

	@Autowired
	BoxRepoImpl boxRepo;
	@Autowired
	ChannelRepoImpl chRepo;
	@Autowired
	TrunkRepoImpl trunkRepo;
	
	public LoadData() {	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void t1loadData() {
		Bank b1p = bankRepo.makePersistent(new Bank(0, 0, "b1", "4.4.4.4", new HashSet<>()));
		Grp g1p = grpRepo.makePersistent(new Grp(0, 0, "group1",new HashSet<>() ,new HashSet<>()));
//		Oper o1p = operRepo.makePersistent(new Oper(0, 0, "ooo1", new HashSet<>()));
		Card c1 = cardRepo.makePersistent(Card.builder().name("ca1").number("2345432543").sernumber("6567567453").place(Place.b0000000)
				.group(g1p).bank(b1p).oper(Oper.GREEN).build());
		b1p.getCards().add(c1);
		g1p.getCards().add(c1);
//		cardRepo.makePersistent(c1);
		
		c1 = cardRepo.makePersistent(Card.builder().name("ca2").number("76325673567").sernumber("8765333").place(Place.b0000001)
				.group(g1p).bank(b1p).oper(Oper.GREEN).build());
		b1p.getCards().add(c1);
		g1p.getCards().add(c1);
//		cardRepo.makePersistent(c1);
		
		c1 = cardRepo.makePersistent(Card.builder().name("ca3").number("11111122222").sernumber("22223333333333").place(Place.b0000002)
				.group(g1p).bank(b1p).oper(Oper.GREEN).build());
		b1p.getCards().add(c1);
		g1p.getCards().add(c1);
//		cardRepo.makePersistent(c1);
		
		Box bo1p = boxRepo.makePersistent(Box.builder().capacity(4).descr("first").disposition("at home").ip("3.3.3.3")
				.channels(new HashSet<>()).build());
////		Trunk t1p = trunkRepo.makePersistent(new Trunk(0, 0, "tr1","descr", new HashSet<>()));
//		Channel ch = chRepo.makePersistent(Channel.builder().box(bo1p).group(g1p).line(Line.L1).name("b1ch1").trunk(t1p).build());
//		bo1p.getChannels().add(ch);
//		ch.setCard(c1);
//		c1.setChannel(ch);
//		cardRepo.makePersistent(c1);
		
		
	}

}
