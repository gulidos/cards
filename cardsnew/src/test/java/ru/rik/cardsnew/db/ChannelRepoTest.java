package ru.rik.cardsnew.db;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Line;
import ru.rik.cardsnew.domain.Oper;
import ru.rik.cardsnew.domain.Route;
import ru.rik.cardsnew.domain.Trunk;

public class ChannelRepoTest {

	private ChannelRepo repo;
	private Trunk t;
	private Box box;
	private Channel ch;
	private Grp g;
	public ChannelRepoTest() {	}
	
	@Before
	public void loadData() {
		repo = spy(new ChannelRepoImpl());
		ChannelRepoImpl.set(repo);
		initData();
		doReturn(new ArrayList<Channel>(t.getChannels())).when(repo).findAll();
	}
	
	@Test
	public void allActiveAndRotating() {
		Route route = Route.builder().oper(Oper.RED).regcode(77).build();
		List<Channel> list = repo.getSorted(t, "79168770925", route);
		for(Channel ch: list) System.out.println(ch.toString());
		
		Assert.assertTrue(list.size() == 8); !!! доделать нет CardStat.getRemain
		for (Channel c : list) 	System.out.print(c.getId());
		Assert.assertTrue(list.get(0).getId() == 1);
		
		System.out.println();
		list = repo.getSorted(t, "111", route);
		for (Channel c : list) 	System.out.print(c.getId());
		Assert.assertTrue(list.get(0).getId() == 2);
		
		Assert.assertTrue(repo.getSorted(t, "111", route).get(0).getId() == 3);
		Assert.assertTrue(repo.getSorted(t, "111", route).get(0).getId() == 4);
		Assert.assertTrue(repo.getSorted(t, "111", route).get(0).getId() == 5);
		Assert.assertTrue(repo.getSorted(t, "111", route).get(0).getId() == 6);
		Assert.assertTrue(repo.getSorted(t, "111", route).get(0).getId() == 7);
		Assert.assertTrue(repo.getSorted(t, "111", route).get(0).getId() == 8);
		Assert.assertTrue(repo.getSorted(t, "111", route).get(0).getId() == 1);
		Assert.assertTrue(repo.getSorted(t, "111", route).get(0).getId() == 2);
	}
	
	@Test
	public void notActiveNotRotating() {
		Route route = Route.builder().oper(Oper.RED).regcode(77).build();
		List<Channel> list = repo.getSorted(t, "111", route);
		list.get(0).setEnabled(false);
		list = repo.getSorted(t, "111", route);
		Assert.assertTrue(list.size() == 7);
	}
	
	@Test
	public void setAllNotActiveReturnEmpty() {
		Route route = Route.builder().oper(Oper.RED).regcode(77).build();
		List<Channel> list = repo.getSorted(t, "111", route);
		for (Channel ch : list) ch.setEnabled(false);
		
		list = repo.getSorted(t, "111", route);
		Assert.assertTrue(list.size() == 0);
	}
	
	@Test
	public void ifCardIsNullNotTake() {
		Route route = Route.builder().oper(Oper.RED).regcode(77).build();
		List<Channel> list = repo.getSorted(t, "111", route);
		list.get(0).setCard(null);
		
		list = repo.getSorted(t, "111", route);
		Assert.assertTrue(list.size() == 7);
	}
	
	@Test
	public void ifStatIdNotReadyNotTake() {
		repo.findStateById(1).setStatus(Status.Failed);
		repo.findStateById(2).setStatus(Status.Inchange);
		repo.findStateById(3).setStatus(Status.Unreach);
		List<Channel> list = repo.getSorted(t, "111", Route.builder().oper(Oper.RED).regcode(77).build());
		Assert.assertTrue(list.size() == 5);
	}
	
	@Test
	public void ifTimeOfCardInChanIsOverSkip() {
		Route route = Route.builder().oper(Oper.RED).regcode(77).build();
		List<Channel> list = repo.getSorted(t, "111", route);
		Assert.assertTrue(list.size() == 8);
		
		Card c = mock(Card.class);
		CardStat cs = mock(CardStat.class);
		when(c.getStat()).thenReturn(cs);
		when(cs.getMinRemains()).thenReturn(0);
		ch.setCard(c);
		
		list = repo.getSorted(t, "111", route);
		Assert.assertTrue(list.size() == 7);
	}
	
	
	private void initData() {
		t = new Trunk(1, 2, "t1", "t1", null, Oper.RED);
		box = new Box(1, 1, "ddd", "1.1.1.1", 8, "qweqwe", null);
		g = Grp.builder().id(1).name("g1").oper(Oper.RED).build();
		Set<Channel> s = new HashSet<>();
		Card c = mock(Card.class);
		CardStat cs = mock(CardStat.class);
		when(c.getStat()).thenReturn(cs);
		when(c.getDlimit()).thenReturn(60);
		when(cs.getMinRemains()).thenReturn(12);
//		cs.setCard(c);
		
		Channel ch1  = new Channel(1, 1, "ch1", Line.L1, box, g, new HashSet<Trunk>(Arrays.asList(t)), c,	true);
		s.add(ch1);
		ChannelState st = repo.addStateIfAbsent(ch1);
		st.setStatus(Status.Ready);
		
		Channel ch2 = new Channel(2, 1, "ch2", Line.L2, box, g, new HashSet<Trunk>(Arrays.asList(t)), c, true);
		s.add(ch2);
		st = repo.addStateIfAbsent(ch2);
		st.setStatus(Status.Ready);
		
		Channel ch3 = new Channel(3, 1, "ch3", Line.L3, box, g, new HashSet<Trunk>(Arrays.asList(t)),c, true);
		s.add(ch3);
		st = repo.addStateIfAbsent(ch3);
		st.setStatus(Status.Ready);
		
		Channel ch4 = new Channel(4, 1, "ch4", Line.L4, box, g, new HashSet<Trunk>(Arrays.asList(t)), c, true);
		s.add(ch4);
		st = repo.addStateIfAbsent(ch4);
		st.setStatus(Status.Ready);
		
		Channel ch5 = new Channel(5, 1, "ch5", Line.L5, box, g, new HashSet<Trunk>(Arrays.asList(t)), c, true);
		s.add(ch5);
		st = repo.addStateIfAbsent(ch5);
		st.setStatus(Status.Ready);
		
		Channel ch6 = new Channel(6, 1, "ch6", Line.L6, box, g, new HashSet<Trunk>(Arrays.asList(t)), c, true);
		s.add(ch6);
		st = repo.addStateIfAbsent(ch6);
		st.setStatus(Status.Ready);
		
		Channel ch7 = new Channel(7, 1, "ch7", Line.L7, box, g, new HashSet<Trunk>(Arrays.asList(t)), c, true);
		s.add(ch7);
		st = repo.addStateIfAbsent(ch7);
		st.setStatus(Status.Ready);
		
		Channel ch8 = new Channel(8, 1, "ch8", Line.L8, box, g, new HashSet<Trunk>(Arrays.asList(t)), c, true);
		s.add(ch8);
		st = repo.addStateIfAbsent(ch8);
		st.setStatus(Status.Ready);

		t.setChannels(s);
		box.setChannels(s);
	}
}
