package ru.rik.cardsnew.db;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.ConfigJpaH2;
import ru.rik.cardsnew.domain.Oper;
import ru.rik.cardsnew.domain.Route;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigJpaH2.class)
public class RouteTests {
	@Autowired private  RouteRepo routes;
	

	public RouteTests() {	}
	

	
	
	@Test @Transactional
	public void returnProperRoutes() {
		Route r = routes.find(9168000000l);
		Assert.assertEquals(r, new Route(9160000000l, 9169999999l, Oper.RED, 77));
		
		r = routes.find(9170000111l);
		Assert.assertTrue(r.getOper() == Oper.RED && r.getRegcode() != 77);
		
		r = routes.find(9250000111l);
		Assert.assertTrue(r.getOper() == Oper.GREEN && r.getRegcode() == 77);
		
		r = routes.find(9204700123L);
		Assert.assertTrue(r.getOper() == Oper.GREEN && r.getRegcode() != 77);
	}
	
	
	@Test @Transactional
	public void returnNullRoute() {
		Route r = routes.find(9998000000l);
		Assert.assertTrue(r.getOper() == Oper.UNKNOWN && r.getRegcode() == 0);	}

}
