package ru.rik.cardsnew;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.Line;
import ru.rik.cardsnew.domain.repo.ChannelsStates;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Test2Config.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChannelsSort {
	@Autowired ChannelsStates chSt;
	public static Set<Channel> chans = new HashSet<>();
	private static Random random = new Random();
	
	public ChannelsSort() {
	}
	

	@Test
	public void t1changeCard() {
		for (int i = 1; i <= 8; i++) {
			Channel ch = Channel.builder().id(i).name("ch" + i).line(Line.getInstance(i)).enabled(true).build();
			ChannelState chs1 = new ChannelState(ch);
			int rnd = Math.abs(random.nextInt()) % 10000;
			chs1.setOrder(rnd);
			chSt.add(chs1);
			chans.add(ch);
		}
	}

	@Test
	public void t2changeCard() {
		String a = "sdadsfa";
		Integer i = 1;
//		Integer.compare(x, y);
		chans.stream()
			.sorted((ch1, ch2) -> Integer.compare(ch1.getState().getOrder(), ch2.getState().getOrder()) )
			.forEach(d->System.out.println(d.toString()));
		
//		for (Channel ch : chans)
//			System.out.println(ch.toString() + " state: " + chSt.findById(ch.getId()));
	}
}
