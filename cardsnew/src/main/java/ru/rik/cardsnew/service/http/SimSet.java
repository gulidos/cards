package ru.rik.cardsnew.service.http;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;
import ru.rik.cardsnew.config.Settings;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.MyState;
import ru.rik.cardsnew.service.TaskDescr;



public class SimSet implements MyState{
	private static final Logger logger = LoggerFactory.getLogger(SimSet.class);		

	@Getter private final Channel ch;
	@Getter private final String cardPos;
	@Getter private final String bankIp;
	@Getter @Setter private SimSet pairData;
//	@Getter private final String serialnumber;
	
	@Builder
	public SimSet(Channel ch, String cardPos, String bankIp, SimSet pairData) {
		this.ch = ch;
		this.cardPos = cardPos;
		this.bankIp = bankIp;
		this.pairData = pairData;
//		this.serialnumber = snumber;
	}
	
	@Override 	public long getId() {return ch.getId(); }
	@Override 	public String getName() {return ch.getName(); }
	
	 /**
     * Gets a SimSet data from channel 
     * @param ch the channel we are interested in 
     * @param chPair the pair channel
     * @return SimSet object
     */
	public static SimSet get(final Channel ch, Channel chPair, TaskDescr td) throws IOException {
		Assert.notNull(ch);
		td.setStage("Executing ...");
		HttpHelper.getCon(ch, "login.cgi")
		.timeout(500)
		.data("cookieexists", "false")
		.data("user", "voip")
		.data("pass", "1234")
		.post(); 		// login for the boxes with old software
		
		Connection con = HttpHelper.getCon(ch, "SimSet.htm");
		Document doc = con.get();
		SimSetBuilder bMy = SimSet.builder();
		SimSetBuilder bPair = SimSet.builder();
		bMy.ch = ch;
		if (chPair != null)
			bPair.ch = chPair;	
		if (ch.getLine().getNport() == 0) {
			Element imob = doc.select("input[name=cIDA]").first();
			bMy.cardPos(imob != null ? imob.attributes().get("value") : "");
			imob = doc.select("input[name=BnkA]").first();
			bMy.bankIp(imob != null ? imob.attributes().get("value") : "");		
			if (chPair != null) {
				imob = doc.select("input[name=cIDB]").first();
				bPair.cardPos(imob != null ? imob.attributes().get("value") : "");
				imob = doc.select("input[name=BnkB]").first();
				bPair.bankIp(imob != null ? imob.attributes().get("value") : "");
			}
		} else { //nPort = 1
			Element imob = doc.select("input[name=cIDB]").first();
			bMy.cardPos(imob != null ? imob.attributes().get("value") : "");
			imob = doc.select("input[name=BnkB]").first();
			bMy.bankIp(imob != null ? imob.attributes().get("value") : "");
			if (chPair != null) {
				imob = doc.select("input[name=cIDA]").first();
				bPair.cardPos(imob != null ? imob.attributes().get("value") : "");
				imob = doc.select("input[name=BnkA]").first();
				bPair.bankIp(imob != null ? imob.attributes().get("value") : "");
			}
		}
		SimSet me = bMy.build();
		if (chPair != null) 
			me.setPairData(bPair.build());
		return me;
	}
	
	
	public static String getScomGsn (Channel ch) throws IOException {
		Connection con = HttpHelper.getCon(ch, "ScomGsn.cgi")
				.method(Method.POST)
				.data("nPortNum", String.valueOf(ch.getLine().getNport()))
				.data("submit", "Submit");
		
		Document doc = con.post();
		Element imob = doc.select("input[name=CURR]").first();
		String r = imob.attributes().get("value");
		System.out.println(r);
		return r;
	}

	
	public static int post(Channel ch, Card c) throws IOException {
		Assert.notNull(ch);
		
		Connection con = HttpHelper.getCon(ch, "SimSet.cgi") .method(Method.POST);
		if (ch.getLine().getNport() == 0) 
			con.data("cIDA", c != null ? c.getPlace().name() : Settings.FAKE_CARD_PLACE)
			   .data("BnkA", c != null ? c.getBank().getName() : Settings.FAKE_BANK_IP);
		else 
			con.data("cIDB", c != null ? c.getPlace().name() : Settings.FAKE_CARD_PLACE)
			   .data("BnkB", c != null ? c.getBank().getName() : Settings.FAKE_BANK_IP);
		Response resp = con.execute();
		
		if (c != null)
			resp =  HttpHelper.getCon(ch, "ScomGsn.cgi").method(Method.POST)
				.data("nPortNum", String.valueOf(ch.getLine().getNport()))
				.data("NEW", c.getSernumber())
				.data("submit", "Submit")
				.execute();
		if (resp.statusCode() != 200) throw new RuntimeException("Can not change IMEI in channel " + ch.getName());
		
		resp =  HttpHelper.getCon(ch, "save.cgi").method(Method.POST)
				.data("submit", "Save")
				.execute();
	
		return resp.statusCode();
	}
	
	
	@Override public Class<?> getClazz() {return SimSet.class;}

	@Override public void setId(long id) {}

	@Override public void setName(String name) {}

	@Override
	public String toString() {
		return "SimSet [" + ch.getId() 
		+ " cardPos=" + cardPos + ", "
				+ "bankIp=" + bankIp + "]";
	}

	
}
