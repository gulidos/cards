package ru.rik.cardsnew.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.ConfigJpaLite;
import ru.rik.cardsnew.domain.Sms;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigJpaLite.class)

public class SmsTest {
	@Autowired private ChannelRepo chans;

	public SmsTest() {	}

	@Test
	@Transactional
	@Rollback(false)
	public void getSms() throws InterruptedException {
		List<Sms> list = new ArrayList<>();
		Sms sms = Sms.builder().encodedmsg("привет").date(new Date()).build();
		list.add(sms);
		chans.smsSave(list);
		
	}
}
