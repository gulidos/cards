package ru.rik.cardsnew.domain;


import java.io.IOException;
import java.net.SocketException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.db.JpaConfig;
import ru.rik.cardsnew.service.TaskDescr;
import ru.rik.cardsnew.service.telnet.TelnetHelperImpl;
import ru.rik.cardsnew.service.telnet.UssdTask;
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = ConfigJpaH2.class)
public class UssdTest {
	@Autowired private  ChannelRepo chans;
	static String encoded = "0421043F0430044104380431043E0020043704300020043E04310440043004490435043D0438043500210020041C044B0020043D0430043F0440043004320438043C0020043E04420432043504420020043D04300020041204300448002004370430043F0440043E04410020043200200053004D0053";
	static String decodedRed = "Спасибо за обращение! Мы направим ответ на Ваш запрос в SMS";

	static String encodedRespRed = "+CUSD: 0,\"0421043F0430044104380431043E0020043704300020043E0431044004300449043504" +
	"3D0438043500210020041C044B0020043D0430043F0440043004320438043C0020043E0442043204" +
	"3504420020043D04300020041204300448002004370430043F0440043E0441002004320020005300" +
	"4D0053\",72";
	
	
	static String encodedRespGreen = "+CUSD: 0,\"003100300035002E003900390440002E000A0421043C043E0442044004380442043500" +
	"2004410430043C043E043500200438043D04420435044004350441043D043E043500200432043804" +
	"340435043E0021002004220440043004440438043A0020043104350441043F043B04300442043D04" +
	"3E0020002800380440002F04340029002A0032003100330023\",72";
	static String decodedGreen =  "105.99р.\n" + "Смотрите самое интересное видео! Трафик бесплатно (8р/д)*213#";
	
	static String encodedRespYellow = "+CUSD: 0,\"04110430043B0430043D04410020003100370034002E0036003000200440002E041204" +
	"3D0438043C0430043D0438043500210020041F043E0445043E043B043E04340430043D0438043500" +
	"210020041F0440043E0433043D043E04370020043F043E0433043E0434044B002000370434043D00" +
	"2E043104350441043F043B00210020041F043E0434043A043B003A002A0033003000390023\",72";
	static String decodedYellow = "Баланс 174.60 р.Внимание! Похолодание! Прогноз погоды 7дн.беспл! Подкл:*309#";
	
	
	
	@Test
	public void encoderTest() {
		UssdTask task = new UssdTask();
		String res = task.encodeRequest("*100#");
		Assert.assertEquals("002a0031003000300023", res);
	}
	
	@Test
	public void encodeFullRespTest() {
		UssdTask task = new UssdTask();
		task.setEncodedResp(encodedRespRed);
		String decodedResp = task.getDecodedResponse();
		Assert.assertEquals(decodedResp, decodedRed);
		
		task.setEncodedResp(encodedRespGreen);
		decodedResp = task.getDecodedResponse();
		Assert.assertEquals(decodedResp, decodedGreen);
	}
	
	@Test
	public void getGreenBalance() throws SocketException, IOException {
		UssdTask task = new UssdTask();
		Channel ch = Channel.builder().group(Grp.builder().oper(Oper.GREEN).build()).build();
		task.setCh(ch);
		task.setCard(new Card());
		task.setDecodedResp(decodedGreen);
		Assert.assertEquals(Float.valueOf(task.getBalance().getBalance()), 105.99f, 0.1);
	}
	
	@Test
	public void getGreenNegativeBalance() throws SocketException, IOException {
		UssdTask task = new UssdTask();
		Channel ch = Channel.builder().group(Grp.builder().oper(Oper.GREEN).build()).build();
		task.setCh(ch);
		task.setCard(new Card());
		task.setDecodedResp("-" + decodedGreen);
		Assert.assertEquals(Float.valueOf(task.getBalance().getBalance()), -105.99f, 0.1);
	}
	
	@Test
	public void getYellowBalance() throws SocketException, IOException {
		UssdTask task = new UssdTask();
		Channel ch = Channel.builder().group(Grp.builder().oper(Oper.YELLOW).build()).build();
		task.setCh(ch);
		task.setCard(new Card());
		task.setDecodedResp(decodedYellow);
		Assert.assertEquals(Float.valueOf(task.getBalance().getBalance()), 174.60f, 0.1);
		task.setDecodedResp("Balans 174.60 р.Внимание! Похолодание");
		Assert.assertEquals(Float.valueOf(task.getBalance().getBalance()), 174.60f, 0.1);
	}

	@Test
	public void getYellowNegativeBalance() throws SocketException, IOException {
		UssdTask task = new UssdTask();
		Channel ch = Channel.builder().group(Grp.builder().oper(Oper.YELLOW).build()).build();
		task.setCh(ch);
		task.setCard(new Card());
		task.setDecodedResp("Минус 174.60 р.Внимание! Похолодание");

		Assert.assertEquals(Float.valueOf(task.getBalance().getBalance()), -174.60f, 0.1);
		
		task.setDecodedResp("Minus 174.60 р.Внимание! Похолодание");
		Assert.assertEquals(Float.valueOf(task.getBalance().getBalance()), -174.60f, 0.1);
		task.setDecodedResp("- 174.60 р.Внимание! Похолодание");
		Assert.assertEquals(Float.valueOf(task.getBalance().getBalance()), -174.60f, 0.1);
		
		task.setDecodedResp("Баланс 92.30 р.Внимание! Похолодание! Прогноз погоды 7дн.беспл! Подкл:*309#");
		Assert.assertEquals(Float.valueOf(task.getBalance().getBalance()), 92.30f, 0.1);
	}
	
	@Test
	public void smsIsNeededTest() {
		UssdTask task = new UssdTask();
		Channel ch = Channel.builder()
				.group(Grp.builder().oper(Oper.RED).build())
				.build();
		task.setCh(ch);
		task.setCard(new Card());
		task.setDecodedResp(decodedRed);
		Assert.assertEquals(task.getBalance().isSmsNeeded(), true);
	}
	
	@Test
	public void ifGarbageGotAnswerisNull() {
		UssdTask task = new UssdTask();
		Channel ch = Channel.builder().group(Grp.builder().oper(Oper.RED).build()).build();
		task.setCh(ch);
		task.setCard(new Card());
		task.setDecodedResp("dswe;o89hv;aoiho;vihaew;origsmdps'p0t");
		Assert.assertNull(task.getBalance().getBalance());
	}
	
	
	public static void main(String[] args) throws SocketException, IOException {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(JpaConfig.class);
		ChannelRepo chans= ctx.getBean(ChannelRepo.class);
		Channel ch = chans.findByName("bln33");		
		Card c = ch.getCard();
		TelnetHelperImpl th = new TelnetHelperImpl();
		TaskDescr td = new TaskDescr(UssdTask.class, ch.getState(chans), new Date()); 
		UssdTask task = UssdTask.get(th, ch, new Card(), "*100#", td);

		System.out.println(task.getDecodedResponse());
		System.out.println(task.getEncodedResp());
		System.out.println(task.getBalance());
	}
}
