package ru.rik.cardsnew.http;

import javax.transaction.Transactional;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.rik.cardsnew.TestConfig;
import ru.rik.cardsnew.db.ChannelRepoImpl;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.service.http.HttpHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GetChInfo {
	@Autowired
	ChannelRepoImpl chans;
	@Autowired
	HttpHelper httpHelper;

	public static long chanId = 1;

	public GetChInfo() {
	}

	@Test
	@Transactional
	@Rollback(false)
	public void t1changeCard() {

		for (Channel ch : chans.findAll()) {

			try {
				httpHelper.getGsmStatus(ch);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(ch.toString() + " stat: " + ch.getState().getStatus() + " date: "
					+ ch.getState().getLastUpdate().toString());
		}

	}
}
