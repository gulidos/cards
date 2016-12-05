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
public class BankRebootTask implements State {
	private static final Logger logger = LoggerFactory.getLogger(BankRebootTask.class);		

	private long id;
	private String name;
	private boolean success;
	private TaskDescr taskDescr;
	
	@Builder
	public BankRebootTask(long id, String name, boolean success, TaskDescr taskDescr) {
		super();
		this.id = id;
		this.name = name;
		this.success = success;
		this.taskDescr = taskDescr;
	}


	
	public static BankRebootTask doIt(Bank b, TaskDescr td) throws IOException  {
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
			return BankRebootTask.builder().id(b.getId()).name(b.getName()).success(true).taskDescr(td).build();
		else 
			return BankRebootTask.builder().id(b.getId()).name(b.getName()).success(false).taskDescr(td).build();
	}



	@Override
	public Class<?> getClazz() {
		return BankRebootTask.class;
	}
	

}
