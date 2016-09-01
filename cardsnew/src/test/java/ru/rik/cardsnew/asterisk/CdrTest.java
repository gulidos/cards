package ru.rik.cardsnew.asterisk;

import java.util.Map.Entry;
import java.util.SortedMap;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.rik.cardsnew.TestConfig;
import ru.rik.cardsnew.domain.events.Cdr;
import ru.rik.cardsnew.domain.repo.Cdrs;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class CdrTest {
@Autowired Cdrs cdrs;
	public CdrTest() {}

	@Test
	public void t1changeCard() {
		SortedMap<String, Cdr>  cs = cdrs.findCdrByCards(6);
		for (Entry<String, Cdr> e: cs.entrySet()) {
			System.out.println(e.getValue());
		}
		try {
			Thread.sleep(500000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
