package ru.rik.cardsnew.service.http;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;
import lombok.experimental.Builder;
import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.State;
import ru.rik.cardsnew.service.TaskDescr;
@Data
public class BankStatusTask implements State {
	private static final Logger logger = LoggerFactory.getLogger(BankStatusTask.class);		

	private long id;
	private String name;
	private int activecards;
	private TaskDescr taskDescr;
	
	@Builder
	public BankStatusTask(long id, String name, int cardcount, TaskDescr td) {
		super();
		this.id = id;
		this.name = name;
		this.activecards = cardcount;
		this.taskDescr = td;
	}

	public static BankStatusTask get(final Bank b, TaskDescr td) throws IOException, InterruptedException {
		if (b == null) 
			throw new NullPointerException("Bank must not be null!");
		td.setStage("Getting bank pages ");

		BankStatusTaskBuilder bld  = BankStatusTask.builder(); 
		bld.id(b.getId())
		.name(b.getName())
		.td(td);
		String host = b.getName();
		String port = Integer.toString(80);
		String login = Bank.DEF_USER + ":" + Bank.DEF_PASSWORD;
		String authString = new String(Base64.encodeBase64(login.getBytes()));
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
		logger.debug(td.getName() + " " + td.toString());
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
	
	public static boolean reboot(Bank b, TaskDescr td) throws IOException  {
		if (b == null) 
			throw new NullPointerException("Bank must not be null!");
		td.setStage("Rebooting bank ");
		String host = b.getName();
		String port = Integer.toString(80);
		String login = Bank.DEF_USER + ":" + Bank.DEF_PASSWORD;
		String authString = new String(Base64.encodeBase64(login.getBytes()));
		Document doc = Jsoup.connect("http://" + host + ":" + port + "/" + "rebootsys.cgi")
				.header("Authorization", "Basic " + authString)
				.timeout(10000).followRedirects(true)
				.data("cookieexists", "false")
				.data("submit", "Reboot")
				.post();
		if (doc.html().contains("Please wait for a moment while rebooting"))
			return true;
		else 
			return false;
	}
	
	
//	http://stackoverflow.com/questions/24772828/how-to-parse-html-table-using-jsoup
	@Override
	public Class<?> getClazz() {return BankStatusTask.class;}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Bank b = Bank.builder().name("72.0.202.19").id(1).build();
		reboot(b, new TaskDescr());
				
	}


}
