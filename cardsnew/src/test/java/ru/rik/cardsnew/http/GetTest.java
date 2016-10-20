package ru.rik.cardsnew.http;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GetTest {
	
	public GetTest() {
		
	}

	
	public void t1test() {
		String hostname = "192.168.200.45";
		String username = "voip";
		String password = "1234";
		String login = username + ":" + password;
		String encodedString = new String(Base64.encodeBase64(login.getBytes()));
		try {
			 
			 
			Connection con = Jsoup.connect("http://192.168.200.45:8080/gsmstatus.cgi")
					.header("Authorization", "Basic " + encodedString)
					.followRedirects(true);
			Document doc = null;
			try {
				 doc = con.data("nPortNum", "1").post();
			} catch (HttpStatusException  e) {
				e.printStackTrace();
			}
			
//			System.out.println(doc.html());	
			Element imob  = doc.select("input[name=COPS]").first();	
			System.out.println("Operator: " + (imob != null ?  imob.attributes().get("value") : ""));
			
			imob  = doc.select("input[name=SCID]").first();
			System.out.println("Card ID: " + (imob != null ?  imob.attributes().get("value") : ""));
			
			imob  = doc.select("input[name=CSQ]").first();
			System.out.println("Signal Quality: " + (imob != null ?  imob.attributes().get("value") : ""));

			imob  = doc.select("input[name=CREG]").first();
			System.out.println("Registration State: " + (imob != null ?  imob.attributes().get("value") : ""));

			imob  = doc.select("input[name=CGSN]").first();
			System.out.println("S/N: " + (imob != null ?  imob.attributes().get("value") : ""));
			
			imob  = doc.select("input[name=MSTT]").first();
			System.out.println("Motion State: " + (imob != null ?  imob.attributes().get("value") : ""));
			
			imob  = doc.select("input[name=IURL]").first();
			System.out.println("Incoming URL: " + (imob != null ?  imob.attributes().get("value") : ""));
			
			imob  = doc.select("input[name=INAME]").first();
			System.out.println("Incoming Name: " + (imob != null ?  imob.attributes().get("value") : ""));
	
			imob  = doc.select("input[name=OIP]").first();
			System.out.println("Outgoing IP: " + (imob != null ?  imob.attributes().get("value") : ""));
			
			imob  = doc.select("input[name=IMOB]").first();
			System.out.println("Incoming Mob: " + (imob != null ?  imob.attributes().get("value") : ""));

			imob  = doc.select("input[name=OMOB]").first();
			System.out.println("Outgoing Mob: " + (imob != null ?  imob.attributes().get("value") : ""));

			System.out.println("======== SIMSet=========");
			
			doc =  Jsoup.connect("http://192.168.200.45:8080/login.cgi")
					.data("cookieexists", "false")
					.data("user", "voip")
					.data("pass", "1234")
					.header("Authorization", "Basic " + encodedString)
					.followRedirects(true)
					.post();
			System.out.println(doc.toString());

			doc =  Jsoup.connect("http://192.168.200.45:8080/SimSet.htm")	
			.header("Authorization", "Basic " + encodedString)
			.followRedirects(true)
			.get();
			
			System.out.println(doc.toString());
			 
			 imob  = doc.select("input[name=mIDA]").first();	
			 System.out.println("A Mobile: " + (imob != null ?  imob.attributes().get("value") : "-"));
			
			 imob  = doc.select("input[name=cIDA]").first();	
			 System.out.println("A Card: " + (imob != null ?  imob.attributes().get("value") : "-"));
				
			 imob  = doc.select("input[name=BnkA]").first();	
			 System.out.println("A Bank URL: " + (imob != null ?  imob.attributes().get("value") : "-"));
			 
			 imob  = doc.select("input[name=mIDB]").first();	
			 System.out.println("B Mobile: " + (imob != null ?  imob.attributes().get("value") : "-"));
			
			 imob  = doc.select("input[name=cIDB]").first();	
			 System.out.println("B Card: " + (imob != null ?  imob.attributes().get("value") : "-"));
				
			 imob  = doc.select("input[name=BnkB]").first();	
			 System.out.println("B Bank URL: " + (imob != null ?  imob.attributes().get("value") : "-"));
//			 
			
			 System.out.println("======== SIP=========");
			 doc =  Jsoup.connect("http://192.168.200.45:8180/servdompage.cgi")
						.header("Authorization", "Basic " + encodedString)
						.followRedirects(true)
						.data("PageNum", "0")
						.post();
			 
			 imob  = doc.select("input[name=DN0]").first();	
			 System.out.println("Display Name: " + (imob != null ?  imob.attributes().get("value") : "-"));
			 
			 imob  = doc.select("input[name=UN0]").first();	
			 System.out.println("User Name: " + (imob != null ?  imob.attributes().get("value") : "-"));
			 
			 imob  = doc.select("input[name=RN0]").first();	
			 System.out.println("Register Name: " + (imob != null ?  imob.attributes().get("value") : "-"));
//			 System.out.println(doc.html());
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
