package ru.rik.cardsnew.service.http;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.MyState;



public class SimSet implements MyState{
	private static final Logger logger = LoggerFactory.getLogger(SimSet.class);		

	@Getter private final Channel ch;
	@Getter private final String cardPos;
	@Getter private final String bankIp;
	@Getter @Setter private SimSet pairData;
	
	@Builder
	public SimSet(Channel ch, String cardPos, String bankIp, SimSet pairData) {
		this.ch = ch;
		this.cardPos = cardPos;
		this.bankIp = bankIp;
		this.pairData = pairData;
	}
	
	@Override 	public long getId() {return ch.getId(); }
	@Override 	public String getName() {return ch.getName(); }
	
	
	public static SimSet get(final Channel ch, Channel chPair) throws IOException {
		Assert.notNull(ch);
		if (chPair != null ) logger.debug("chId: {} chName: {}", chPair.getId(), chPair.getName());
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

	@Override
	public Class<?> getClazz() {return SimSet.class;}

	@Override
	public void setId(long id) {	
	}

	@Override
	public void setName(String name) {		
	}

	@Override
	public String toString() {
		return "SimSet [" + ch.getId() 
		+ " cardPos=" + cardPos + ", "
				+ "bankIp=" + bankIp + "]";
	}

	
}
