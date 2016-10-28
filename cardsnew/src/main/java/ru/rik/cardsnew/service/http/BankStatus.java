package ru.rik.cardsnew.service.http;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.Data;
import lombok.experimental.Builder;
import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.State;
@Data
public class BankStatus implements State {
	private long id;
	private String name;
	private int cardcount;
	
	@Builder
	public BankStatus(long id, String name, int cardcount) {
		super();
		this.id = id;
		this.name = name;
		this.cardcount = cardcount;
	}

	public static void get(final Bank b) throws IOException {
		if (b == null) throw new NullPointerException("Bank must not be null!");
		
		String host = b.getName();
		String port = Integer.toString(80);
		String login = Bank.DEF_USER + ":" + Bank.DEF_PASSWORD;
		String authString = new String(Base64.encodeBase64(login.getBytes()));
		Connection con = Jsoup.connect("http://" + host + ":" + port + "/" + "SimStatus.cgi")
				.header("Authorization", "Basic " + authString).timeout(10000).followRedirects(true)
				.data("PageNum", String.valueOf(0))
				.data("submit", "Submit");
		
		Document doc = con.post();
		System.out.println(doc.body());
		Element table = doc.select("table").get(0); // select the first table.
		Elements rows = table.select("tr");
		for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
			 Element row = rows.get(i);
			 System.out.println(row.toString());
//		     Elements cols = row.select("td");
		     
		}
//		Element imob = doc.select("input[name=CURR]").first();
//		String r = imob.attributes().get("value");
		
	}
	
//    <tr id="Item" bgcolor="#ddffff" style="color:000000"> 
//    <!-- <td><input type=checkbox name=ON value=6 ssssssss> --> 
//    <td id="Num">7 </td>
//    <td id="CoS">3/2 </td>
//    <td id="SimID">b0000007 </td>
//    <td id="GsmID">- </td>
//    <td id="remote">- </td>
//   </tr>
//   <tr id="Item" bgcolor="#ccffff" style="color:0000FF"> 
//    <!-- <td><input type=checkbox name=ON value=7 ssssssss> --> 
//    <td id="Num">8 </td>
//    <td id="CoS">4/2 </td>
//    <td id="SimID">b0000008 </td>
//    <td id="GsmID">a0000001 </td>
//    <td id="remote">172.17.1.36:1204 
	@Override
	public Class<?> getClazz() {return BankStatus.class;}
	
	public static void main(String[] args) throws IOException {
		Bank b = Bank.builder().name("72.0.202.4").id(1).build();
		get(b);
	}


}
