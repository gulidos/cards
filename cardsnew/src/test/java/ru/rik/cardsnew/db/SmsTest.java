package ru.rik.cardsnew.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.rik.cardsnew.ConfigJpaH2;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.CardStat;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Sms;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigJpaH2.class)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public class SmsTest {
	@Autowired private ChannelRepo chans;
	@Autowired private CardRepo cards;
	
	public SmsTest() {	}

	@Test
	public void getSms() throws InterruptedException {
		List<Sms> list = new ArrayList<>();
		Sms sms = Sms.builder().decodedmsg("привет").channel(chans.findById(1))
				.card(cards.findById(1)).date(new Date()).build();
		list.add(sms);
		chans.smsHandle(list);
	}
	
	
	@Test
	public void handleSmsWithBalance() {
		List<Sms> list = new ArrayList<>();
		Channel ch = chans.findById(1);
		Card card = cards.findById(1);
		Sms sms = Sms.builder()
				.channel(ch)
				.card(card)
				.decodedmsg("Баланс: -200.11 р , Лимит:0,01р Приятная музыка, ")
				.date(new Date())
				.build();
		list.add(sms);
		chans.smsHandle(list);
		CardStat st = card.getStat(cards);
		Assert.assertEquals(st.getBalance(), -200.11, 0.1);
		
	}
}

	