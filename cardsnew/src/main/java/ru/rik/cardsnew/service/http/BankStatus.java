package ru.rik.cardsnew.service.http;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.Data;
import lombok.experimental.Builder;
import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.State;
import ru.rik.cardsnew.service.TaskDescr;
@Data
public class BankStatus implements State {
	private long id;
	private String name;
	private int activecards;
	private TaskDescr taskDescr;
	
	@Builder
	public BankStatus(long id, String name, int cardcount, TaskDescr td) {
		super();
		this.id = id;
		this.name = name;
		this.activecards = cardcount;
		this.taskDescr = td;
	}

	public static BankStatus get(final Bank b, TaskDescr td) throws IOException, InterruptedException {
		if (b == null) throw new NullPointerException("Bank must not be null!");
		td.setStage("Getting bank pages ");
		BankStatus.BankStatusBuilder bld  = BankStatus.builder(); 
		bld.id(b.getId())
		.name(b.getName())
		.td(td);
		String host = b.getName();
		String port = Integer.toString(80);
		String login = Bank.DEF_USER + ":" + Bank.DEF_PASSWORD;
		String authString = new String(Base64.encodeBase64(login.getBytes()));
//		http://stackoverflow.com/questions/24772828/how-to-parse-html-table-using-jsoup
		int totalAct = 0;
		for (int i = 0; i <= 3; i++) {
			Document doc = Jsoup.connect("http://" + host + ":" + port + "/" + "SimStatus.cgi")
					.header("Authorization", "Basic " + authString)
					.timeout(10000).followRedirects(true)
					.data("PageNum", String.valueOf(i))
					.data("cookieexists", "false")
					.data("submit", "Submit")
					.post();
			
			totalAct += getBankPage(doc);
			TimeUnit.SECONDS.sleep(1);
		}	
		bld.cardcount(totalAct);
		return bld.build();
	}

	private static int getBankPage(Document doc) throws IOException {
		int activeCards = 0;
		Element table = doc.select("table").get(0); // select the first table.
		Elements rows = table.select("tr");
		for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
			 Element row = rows.get(i);

			Elements cols = row.select("td");
			if (cols.size() > 4) {
				Element gsmID = cols.get(3);
				if (!"-".equals(gsmID.text()) )
					activeCards++;
			}  
		}
				return activeCards;
	}
	
	
//	http://stackoverflow.com/questions/24772828/how-to-parse-html-table-using-jsoup
	@Override
	public Class<?> getClazz() {return BankStatus.class;}
	
//	public static void main(String[] args) throws IOException, InterruptedException {
//		Bank b = Bank.builder().name("72.0.202.21").id(1).build();
//		BankStatus bs = BankStatus.get(b);
//		System.out.println(bs.toString());
//	}


}
