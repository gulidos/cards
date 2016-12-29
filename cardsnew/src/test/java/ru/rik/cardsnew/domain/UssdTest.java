package ru.rik.cardsnew.domain;


import java.io.IOException;
import java.net.SocketException;
import java.util.Date;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ru.rik.cardsnew.config.RootConfig;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.service.TaskCompleter;
import ru.rik.cardsnew.service.TaskDescr;
import ru.rik.cardsnew.service.telnet.TelnetHelper;
import ru.rik.cardsnew.service.telnet.UssdTask;
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = ConfigJpaH2.class)
public class UssdTest {
	
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
	
//	+CMGR: 0,,160
//	07919761980612F2400ED0C2303BEC1E971B0008611103220364618C050003ED020104110430043B0430043D0441003A003100380036002C003200300440002C041B0438043C04380442003A0030002C003000310440002004230437043D0430043904420435002C00200433043404350020041204300448043800200431043B04380437043A0438043500200438043B043800200434044004430437044C044F0020043D04300020
//	0
	
	
	@Test
	public void encoderTest() {
		UssdTask task = new UssdTask();
		String res = task.encodeRequest("*100#");
		Assert.assertEquals("002a0031003000300023", res);
		
		Assert.assertEquals("00230031003000300023", task.encodeRequest("#100#"));
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
		Assert.assertEquals(Float.valueOf(task.getUssd().getBalance()), 105.99f, 0.1);
	}
	
	@Test
	public void getGreenNegativeBalance() throws SocketException, IOException {
		UssdTask task = new UssdTask();
		Channel ch = Channel.builder().group(Grp.builder().oper(Oper.GREEN).build()).build();
		task.setCh(ch);
		task.setCard(new Card());
		task.setDecodedResp("-" + decodedGreen);
		Assert.assertEquals(Float.valueOf(task.getUssd().getBalance()), -105.99f, 0.1);
		
		task.setDecodedResp("-105.99р.\n" + "Смотрите самое интересное видео!");
		Assert.assertEquals(Float.valueOf(task.getUssd().getBalance()), -105.99f, 0.1);
	}
	
	@Test
	public void getYellowBalance() throws SocketException, IOException {
		UssdTask task = new UssdTask();
		Channel ch = Channel.builder().group(Grp.builder().oper(Oper.YELLOW).build()).build();
		task.setCh(ch);
		task.setCard(new Card());
		task.setDecodedResp(decodedYellow);
		Assert.assertEquals(Float.valueOf(task.getUssd().getBalance()), 174.60f, 0.1);
		task.setDecodedResp("Balans 174.60 р.Внимание! Похолодание");
		Assert.assertEquals(Float.valueOf(task.getUssd().getBalance()), 174.60f, 0.1);
	}

	@Test
	public void getYellowNegativeBalance() throws SocketException, IOException {
		UssdTask task = new UssdTask();
		Channel ch = Channel.builder().group(Grp.builder().oper(Oper.YELLOW).build()).build();
		task.setCh(ch);
		task.setCard(new Card());
		task.setDecodedResp("Минус 174.60 р.Внимание! Похолодание");

		Assert.assertEquals(Float.valueOf(task.getUssd().getBalance()), -174.60f, 0.1);
		
		task.setDecodedResp("Minus 174.60 р.Внимание! Похолодание");
		Assert.assertEquals(Float.valueOf(task.getUssd().getBalance()), -174.60f, 0.1);
		task.setDecodedResp("- 174.60 р.Внимание! Похолодание");
		Assert.assertEquals(Float.valueOf(task.getUssd().getBalance()), -174.60f, 0.1);
		
		task.setDecodedResp("Баланс 92.30 р.Внимание! Похолодание! Прогноз погоды 7дн.беспл! Подкл:*309#");
		Assert.assertEquals(Float.valueOf(task.getUssd().getBalance()), 92.30f, 0.1);
		
		task.setDecodedResp("Balance:200,10r,Limit:0,01r");
		Assert.assertEquals(Float.valueOf(task.getUssd().getBalance()), 200.10f, 0.1);
		
		task.setDecodedResp("Balance:105r,Limit:0,01r ");
		Assert.assertEquals(Float.valueOf(task.getUssd().getBalance()), 105.00f, 0.1);

		
		task.setDecodedResp("Баланс -123.38 р.Внимание! Похолодание! Прогноз погоды 7дн.беспл! Подкл:*309#");
		Ussd b = task.getUssd();
		System.out.println(b);
		float value = b.getBalance();
		Assert.assertEquals(value, -123.38f, 0.1);
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
		Assert.assertEquals(task.getUssd().isSmsNeeded(), true);
	}
	
	@Test
	public void ifGarbageGotAnswerisNull() {
		UssdTask task = new UssdTask();
		Channel ch = Channel.builder().group(Grp.builder().oper(Oper.RED).build()).build();
		task.setCh(ch);
		task.setCard(new Card());
		task.setDecodedResp("dswe;o89hv;aoiho;vihaew;origsmdps'p0t");
		Assert.assertNull(task.getUssd().getBalance());
	}
	
	@Autowired private  ChannelRepo chans;
	
	public static void main(String[] args) throws SocketException, IOException, InterruptedException {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(RootConfig.class);
		ChannelRepo chans= ctx.getBean(ChannelRepo.class);
		TaskCompleter taskCompleter = ctx.getBean(TaskCompleter.class);
		Channel ch = chans.findByName("bln75");		
		Card c = ch.getCard();
		TelnetHelper th = ctx.getBean(TelnetHelper.class);
		
		TaskDescr td = new TaskDescr(UssdTask.class, ch.getState(chans), new Date());
		System.out.println("Connecting to ...");
//		UssdTask task = UssdTask.get(th, ch, new Card(), "*100#", td);
		Future<State> f = taskCompleter.addTask(()-> UssdTask.get(th, ch, c, "*100#", td), td);
		
		while (!f.isDone()) {
			System.out.println("waiting ...");
			Thread.sleep(1000);
		}
		
		System.out.println("got it");

//		System.out.println(task.getDecodedResponse());
//		System.out.println(task.getEncodedResp());
//		System.out.println(task.getBalance());
	}
}
