package ru.rik.cardsnew.db;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.CardStat;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.ChannelState.Status;
import ru.rik.cardsnew.domain.Line;
import ru.rik.cardsnew.domain.Trunk;

public class ChannelRepoTest {

	private ChannelRepo repo;
	private Trunk t;
	private Box box;
	private Channel ch;
	public ChannelRepoTest() {	}
	
	@Before
	public void loadData() {
		repo = spy(ChannelRepoImpl.class);
		repo.init();
		initData();
	}
	
	@Test
	public void allActiveAndRotating() {
		List<Channel> list = repo.getSorted(t);
		Assert.assertTrue(list.size() == 8);
		for (Channel c : list) 	System.out.print(c.getId());
		Assert.assertTrue(list.get(0).getId() == 1);
		
		System.out.println();
		list = repo.getSorted(t);
		for (Channel c : list) 	System.out.print(c.getId());
		Assert.assertTrue(list.get(0).getId() == 2);
		
		Assert.assertTrue(repo.getSorted(t).get(0).getId() == 3);
		Assert.assertTrue(repo.getSorted(t).get(0).getId() == 4);
		Assert.assertTrue(repo.getSorted(t).get(0).getId() == 5);
		Assert.assertTrue(repo.getSorted(t).get(0).getId() == 6);
		Assert.assertTrue(repo.getSorted(t).get(0).getId() == 7);
		Assert.assertTrue(repo.getSorted(t).get(0).getId() == 8);
		Assert.assertTrue(repo.getSorted(t).get(0).getId() == 1);
		Assert.assertTrue(repo.getSorted(t).get(0).getId() == 2);
	}
	
	@Test
	public void notActiveNotRotating() {
		List<Channel> list = repo.getSorted(t);
		list.get(0).setEnabled(false);
		list = repo.getSorted(t);
		Assert.assertTrue(list.size() == 7);
	}
	
	@Test
	public void setAllNotActiveReturnEmpty() {
		List<Channel> list = repo.getSorted(t);
		for (Channel ch : list) ch.setEnabled(false);
		
		list = repo.getSorted(t);
		Assert.assertTrue(list.size() == 0);
	}
	
	@Test
	public void ifCardIsNullNotTake() {
		List<Channel> list = repo.getSorted(t);
		list.get(0).setCard(null);;
		
		list = repo.getSorted(t);
		Assert.assertTrue(list.size() == 7);
	}
	
	@Test
	public void ifStatIdNotReadyNotTake() {
		repo.findStateById(1).setStatus(Status.Failed);
		repo.findStateById(2).setStatus(Status.Inchange);
		repo.findStateById(3).setStatus(Status.Unreach);
		List<Channel> list = repo.getSorted(t);
		Assert.assertTrue(list.size() == 5);
	}
	
	@Test
	public void ifTimeOfCardInChanIsOverSkip() {
		List<Channel> list = repo.getSorted(t);
		Assert.assertTrue(list.size() == 8);
		
		Card c = mock(Card.class);
		CardStat cs = mock(CardStat.class);
		when(c.getStat()).thenReturn(cs);
		when(cs.getMinRemains()).thenReturn(0);
		ch.setCard(c);
		
		list = repo.getSorted(t);
		Assert.assertTrue(list.size() == 7);
	}
	
	
	private void initData() {
		t = new Trunk(1, 2, "t1", "t1", null);
		box = new Box(1, 1, "ddd", "1.1.1.1", 8, "qweqwe", null);

		Set<Channel> s = new HashSet<>();
		Card c = mock(Card.class);
		CardStat cs = mock(CardStat.class);
		when(c.getStat()).thenReturn(cs);
		when(cs.getMinRemains()).thenReturn(12);
//		cs.setCard(c);
		
		ch = new Channel(1, 1, "ch1", Line.L1, box, null, new HashSet<Trunk>(Arrays.asList(t)), c,	true);
		s.add(ch);
		ChannelState st = repo.addStateIfAbsent(ch);
		st.setStatus(Status.Ready);
		
		ch = new Channel(2, 1, "ch2", Line.L2, box, null, new HashSet<Trunk>(Arrays.asList(t)), c, true);
		s.add(ch);
		st = repo.addStateIfAbsent(ch);
		st.setStatus(Status.Ready);
		
		ch = new Channel(3, 1, "ch3", Line.L3, box, null, new HashSet<Trunk>(Arrays.asList(t)),c, true);
		s.add(ch);
		st = repo.addStateIfAbsent(ch);
		st.setStatus(Status.Ready);
		
		ch = new Channel(4, 1, "ch4", Line.L4, box, null, new HashSet<Trunk>(Arrays.asList(t)), c, true);
		s.add(ch);
		st = repo.addStateIfAbsent(ch);
		st.setStatus(Status.Ready);
		
		ch = new Channel(5, 1, "ch5", Line.L5, box, null, new HashSet<Trunk>(Arrays.asList(t)), c, true);
		s.add(ch);
		st = repo.addStateIfAbsent(ch);
		st.setStatus(Status.Ready);
		
		ch = new Channel(6, 1, "ch6", Line.L6, box, null, new HashSet<Trunk>(Arrays.asList(t)), c, true);
		s.add(ch);
		st = repo.addStateIfAbsent(ch);
		st.setStatus(Status.Ready);
		
		ch = new Channel(7, 1, "ch7", Line.L7, box, null, new HashSet<Trunk>(Arrays.asList(t)), c, true);
		s.add(ch);
		st = repo.addStateIfAbsent(ch);
		st.setStatus(Status.Ready);
		
		ch = new Channel(8, 1, "ch8", Line.L8, box, null, new HashSet<Trunk>(Arrays.asList(t)), c, true);
		s.add(ch);
		st = repo.addStateIfAbsent(ch);
		st.setStatus(Status.Ready);

		t.setChannels(s);
		box.setChannels(s);
	}
}
