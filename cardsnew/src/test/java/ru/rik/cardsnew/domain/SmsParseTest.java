package ru.rik.cardsnew.domain;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;


public class SmsParseTest {

	public SmsParseTest() {	}
	
	@Test
	public void encodeBalanceTest() {
		String s = "Баланс:169,11р,Лимит:0,01р Приятная музыка, интересные истории, ";
		Sms sms = Sms.builder().card(new Card()).date(new Date()).decodedmsg(s).build();
		Balance b = sms.getBalance();
		Assert.assertEquals(b.getBalance(), 169.11f, 0.1);
		
		sms.setDecodedmsg("Баланс:169.11р,Лимит:0,01р Приятная музыка, интересные истории, ");
		Assert.assertEquals(sms.getBalance().getBalance(), 169.11f, 0.1);
		
		sms.setDecodedmsg("Баланс: 169.11 р , Лимит:0,01р Приятная музыка, интересные истории, ");
		Assert.assertEquals(sms.getBalance().getBalance(), 169.11f, 0.1);
		
		sms.setDecodedmsg("Баланс: -169.11 р , Лимит:0,01р Приятная музыка, интересные истории, ");
		Assert.assertEquals(sms.getBalance().getBalance(), -169.11f, 0.1);
	}

}
