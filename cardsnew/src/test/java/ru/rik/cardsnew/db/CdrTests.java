package ru.rik.cardsnew.db;

import java.text.ParseException;
import java.util.SortedMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.rik.cardsnew.domain.TestUtils;
import ru.rik.cardsnew.domain.events.Cdr;
import ru.rik.cardsnew.domain.repo.Cdrs;

public class CdrTests {
	private Cdrs cdrs;
	public CdrTests() {	}
	
	@Before
	public void loadData() throws ParseException {
		cdrs = TestUtils.loadCdrs();
		cdrs.init();
	}

	
	@Test 
	public void findCdrsofCard() {
		SortedMap<String, Cdr> subMap = cdrs.findCdrByCards(1, false);
		Assert.assertTrue(subMap.size() == 4);		
	}

	@Test 
	public void checkAsr () {
		
	}
	
}
