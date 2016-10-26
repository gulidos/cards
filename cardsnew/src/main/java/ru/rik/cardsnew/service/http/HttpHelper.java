package ru.rik.cardsnew.service.http;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Channel;

public class HttpHelper {

	public HttpHelper() {}

	public static Connection getCon(final Channel ch, final String link) {
		if (ch == null) throw new NullPointerException("Channel must not be null!");
		
		String host = ch.getBox().getIp();
		String port = Integer.toString(ch.getLine().getHttpport());
		String user = Box.DEF_USER;
		String password = Box.DEF_PASSWORD;
		String login = user + ":" + password;
		String authString = new String(Base64.encodeBase64(login.getBytes()));
		System.out.println("connect to: " + ch.getName() +  " http://" + host + ":" + port +"/" + link);
		return Jsoup.connect("http://" + host + ":" + port +"/" + link)
				.header("Authorization", "Basic " + authString)
				.timeout(10000)
				.followRedirects(true);
	}
	
	
	public static int rebootChannel(Channel ch) throws IOException {
		Connection con = HttpHelper.getCon(ch, "rebootsys.cgi");
		con.timeout(10000)
		.followRedirects(true)
        .method(Method.POST)
        .data("submit", "Reboot");
		
		Response resp = con.execute();
		return resp.statusCode();
	}
	
	
}
