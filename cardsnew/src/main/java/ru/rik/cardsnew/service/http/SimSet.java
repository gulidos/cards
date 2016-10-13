package ru.rik.cardsnew.service.http;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.MyState;



public class SimSet extends MyState{
	@Getter private final Channel ch;
	@Getter private final String cardPos;
	@Getter private final String bankIp;
	@Getter @Setter private SimSet pairData;
	@Builder
	public SimSet(Channel ch, String cardPos, String bankIp, SimSet pairData) {
		super();
		this.ch = ch;
		this.cardPos = cardPos;
		this.bankIp = bankIp;
		this.pairData = pairData;
	}

	public static SimSet get(final Channel ch) throws IOException {
		Connection con = HttpHelper.getCon(ch, "SimSet.htm");
		System.out.println(ch.toString());

		Document doc = con.get();
		System.out.println(doc.toString());

		SimSetBuilder bMy = SimSet.builder();
		SimSetBuilder bPair = SimSet.builder();
		if (ch.getLine().getNport() == 0) {
			bMy.ch = ch;
			Element imob = doc.select("input[name=cIDA]").first();
			bMy.cardPos(imob.attributes().get("value"));
			imob = doc.select("input[name=BnkA]").first();
			bMy.bankIp(imob.attributes().get("value"));		
			bPair.ch = ch.getPair();
			imob = doc.select("input[name=cIDB]").first();
			bPair.cardPos(imob.attributes().get("value"));
			imob = doc.select("input[name=BnkB]").first();
			bPair.bankIp(imob.attributes().get("value"));
		} else { //nPort = 1
			bMy.ch = ch.getPair();
			Element imob = doc.select("input[name=cIDB]").first();
			bMy.cardPos(imob.attributes().get("value"));
			imob = doc.select("input[name=BnkB]").first();
			bMy.bankIp(imob.attributes().get("value"));
			bPair.ch = ch;
			imob = doc.select("input[name=cIDA]").first();
			bPair.cardPos(imob.attributes().get("value"));
			imob = doc.select("input[name=BnkA]").first();
			bPair.bankIp(imob.attributes().get("value"));
		}
		
		SimSet me = bMy.build();
		me.setPairData(bPair.build());
		return me;
	}

	@Override
	public Class<?> getClazz() {return SimSet.class;}

	
}
