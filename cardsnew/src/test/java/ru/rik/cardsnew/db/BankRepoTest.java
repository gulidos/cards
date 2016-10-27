package ru.rik.cardsnew.db;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.BankState;


public class BankRepoTest {
	private BankRepo repo;
	@Before
	public void loadData() {
		repo = spy(new BankRepoImpl());
		BankRepoImpl.set(repo);
		
		List<Bank> list =  new ArrayList<>();
		Bank b1 = new Bank(1, 1, "1", "1.1.1.1", null);
		BankState bs1 = repo.addStateIfAbsent(b1);
		doReturn(b1).when(repo).findByName("1.1.1.1");
		list.add(b1);
		
		Bank b2 = new Bank(2, 1, "2", "1.1.1.2",  null);
		repo.addStateIfAbsent(b2);
		list.add(b2);
		
		Bank b3 = new Bank(3, 1, "3", "1.1.1.3",  null);
		repo.addStateIfAbsent(b3);
		list.add(b3);
		
		Bank b4 = new Bank(4, 1, "4", "1.1.1.4",  null);
		repo.addStateIfAbsent(b4);
		list.add(b4);
		
		Bank b5 =new Bank(5, 1, "5", "1.1.1.5",  null);
		repo.addStateIfAbsent(b5);
		list.add(b5);
		
		doReturn(list).when(repo).findAll();
//		when(repo.findAll()).thenReturn(list);
	}
	
	
	@Test
	public void test1(){
		repo.findAll();
	}
	
	@Test
	public void findByName(){
		repo.findByName("1.1.1.1");
	}
	
	public BankRepoTest() {	}

}
