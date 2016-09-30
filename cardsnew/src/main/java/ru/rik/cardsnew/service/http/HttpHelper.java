package ru.rik.cardsnew.service.http;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.service.http.GsmState.GsmApp;
import ru.rik.cardsnew.service.http.GsmState.GsmStateBuilder;

public class HttpHelper {

	public HttpHelper() {}

	private Connection getCon(final Channel ch, final String link) throws IOException {
		if (ch == null) throw new NullPointerException("Channel must not be null!");
		
		String host = ch.getBox().getIp();
		String port = Integer.toString(ch.getLine().getHttpport());
		String user = Box.DEF_USER;
		String password = Box.DEF_PASSWORD;
		String login = user + ":" + password;
		String authString = new String(Base64.encodeBase64(login.getBytes()));
		
		return Jsoup.connect("http://" + host + ":" + port +"/" + link)
				.header("Authorization", "Basic " + authString)
				.followRedirects(true);
	}
	
	
	
	public GsmState getGsmState(final Channel ch) throws IOException, IllegalAccessException {
		String nport = Integer.toString(ch.getLine().getNport());
		Connection con = getCon(ch, "gsmstatus.cgi");
	
		
		GsmStateBuilder b = GsmState.builder();

		Document doc = con.data("nPortNum", nport).post();
		
		b.update(new Date());
		
		Element imob = doc.select("input[name=MSTT]").first();
		GsmApp status = GsmApp.getInstance(imob.attributes().get("value"));
		b.status(status);

		imob  = doc.select("input[name=COPS]").first();
		b.operator(imob.attributes().get("value"));
		
		imob  = doc.select("input[name=CSQ]").first();
		String v = imob.attributes().get("value");
		if ( v.length() > 0) {
			int q = Integer.parseInt(v);
			b.sigquality(q);
		}
		
		imob  = doc.select("input[name=CREG]").first();
		b.regstate(imob.attributes().get("value"));

		imob  = doc.select("input[name=CGSN]").first();
		b.sernum(imob.attributes().get("value"));
		
		imob  = doc.select("input[name=IURL]").first();
		b.iURL(imob.attributes().get("value"));
		
		imob  = doc.select("input[name=INAME]").first();
		b.iName(imob.attributes().get("value"));

		imob  = doc.select("input[name=OMOB]").first();
		b.oMob(imob.attributes().get("value"));
		
		return b.build();
	}
	
}
