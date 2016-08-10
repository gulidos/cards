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
import ru.rik.cardsnew.domain.ChannelState.ChStatus;

public class HttpHelper {

	public HttpHelper() {

	}

	private Connection getCon(final Channel ch, final String link) throws IOException {
		if (ch == null) 	throw new NullPointerException("Channel must not be null!");
		
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
	
	
	
	public void getGsmStatus(Channel ch) throws IOException {
		String nport = Integer.toString(ch.getLine().getNport());
		Connection con = getCon(ch, "gsmstatus.cgi");
		
		Document doc = con.data("nPortNum", nport).post();
		Element imob = doc.select("input[name=MSTT]").first();
		ChStatus status = ChStatus.getInstance(imob.attributes().get("value"));
		
		ch.getState().setStatus(status);
		ch.getState().setLastUpdate(new Date());
	}
	
}
