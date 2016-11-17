package ru.rik.cardsnew.domain;

import org.junit.Assert;
import org.junit.Test;

import ru.rik.cardsnew.service.telnet.UssdTask;

public class UssdTest {
	// +CUSD:
	// 0,"0421043F0430044104380431043E0020043704300020043E04310440043004490435043D0438043500210020041C044B0020043D0430043F0440043004320438043C0020043E04420432043504420020043D04300020041204300448002004370430043F0440043E04410020043200200053004D0053",72
	static String encoded = "0421043F0430044104380431043E0020043704300020043E04310440043004490435043D0438043500210020041C044B0020043D0430043F0440043004320438043C0020043E04420432043504420020043D04300020041204300448002004370430043F0440043E04410020043200200053004D0053";
	static String decoded = "Спасибо за обращение! Мы направим ответ на Ваш запрос в SMS";



	@Test
	public void decoderTest() {
		UssdTask task = new UssdTask();
		byte[] responded = task.pduToBytes(encoded);
		String result = task.decodeUcs2Encoding(null, responded);
		System.out.println(result);
		Assert.assertEquals(decoded, result);
	}
	
	@Test
	public void encoderTest() {
		UssdTask task = new UssdTask();
		String res = task.encodeReq("*100#");
		Assert.assertEquals("002a0031003000300023", res);
	}

}
