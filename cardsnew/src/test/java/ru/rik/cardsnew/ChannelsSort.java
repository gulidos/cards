package ru.rik.cardsnew;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.naming.NoPermissionException;
import javax.transaction.Transactional;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.rik.cardsnew.db.ChannelRepoImpl;
import ru.rik.cardsnew.db.TrunkRepoImpl;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.Line;
import ru.rik.cardsnew.domain.Trunk;
import ru.rik.cardsnew.domain.repo.ChannelsStates;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChannelsSort {
	@Autowired ChannelsStates chSt;
	@Autowired ChannelRepoImpl chanRepo;
	@Autowired TrunkRepoImpl trRepo;
	@Autowired TaskExecutor taskExecutor;
	
	public static Set<Channel> chans = new HashSet<>();
	
	
	public ChannelsSort() {
	}
	

//	@Test
	public void t1changeCard() {
		for (int i = 1; i <= 8; i++) {
			Channel ch = Channel.builder().id(i).name("ch" + i).line(Line.getInstance(i)).enabled(true).build();
			ChannelState chs1 = new ChannelState(ch);

			chSt.add(chs1);
			chans.add(ch);
		}
	}

	@Test
	@Transactional
	public void t2changeCard() {
		List<Channel> allCh = chanRepo.findAll();
		Trunk t = trRepo.findById(1L);
		for (int i = 0; i < 15; i++)
			exec(t);
	}

	@Transactional
	@Async
	private void exec(Trunk t) {
		for (int i = 0; i < 10; i++) {
			try {
				for (Channel ch : chanRepo.getSorted(t))
					System.out.println(ch.getName() + "cad: " + ch.getCard().getName() + " : " + ch.getState().getPriority());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	
}
