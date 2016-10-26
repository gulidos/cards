package ru.rik.cardsnew.db;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ru.rik.cardsnew.domain.Bank;


public class BankRepoTest {
	private BankRepo repo;
	@Before
	public void loadData() {
		repo = mock(BankRepoImpl.class);
		List<Bank> list =  new ArrayList<>();
		list.add(new Bank(1, 1, "1", "1.1.1.1", true, null));
		list.add(new Bank(2, 1, "2", "1.1.1.2", true, null));
		list.add(new Bank(3, 1, "3", "1.1.1.3", true, null));
		list.add(new Bank(4, 1, "4", "1.1.1.4", true, null));
		list.add(new Bank(5, 1, "5", "1.1.1.5", true, null));
		when(repo.findAll()).thenReturn(list);
	}
	
	
	@Test
	public void test1(){
		repo.findAll();
	}
	
	public BankRepoTest() {	}

}
