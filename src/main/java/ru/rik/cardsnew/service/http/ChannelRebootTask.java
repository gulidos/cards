package ru.rik.cardsnew.service.http;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;
import lombok.experimental.Builder;
import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.State;
import ru.rik.cardsnew.service.TaskDescr;
@Data
public class ChannelRebootTask implements State {
	private static final Logger logger = LoggerFactory.getLogger(ChannelRebootTask.class);		

	private long id;
	private String name;
	private boolean success;
	private TaskDescr taskDescr;
	
	@Builder
	public ChannelRebootTask(long id, String name, boolean success, TaskDescr taskDescr) {
		super();
		this.id = id;
		this.name = name;
		this.success = success;
		this.taskDescr = taskDescr;
	}


	
	public static ChannelRebootTask doIt(Channel ch, TaskDescr td) throws IOException  {
		if (ch == null) 
			throw new NullPointerException("Channel must not be null!");
		if (td != null)
			td.setStage("Rebooting channel ");
		String host = ch.getBox().getIp();
		String port = Integer.toString(ch.getLine().getHttpport());
		String login = Box.DEF_USER + ":" + Box.DEF_PASSWORD;
		String authString = new String(Base64.encodeBase64(login.getBytes()));
		Document doc = Jsoup.connect("http://" + host + ":" + port + "/" + "rebootsys.cgi")
				.header("Authorization", "Basic " + authString)
				.timeout(10000).followRedirects(true)
				.data("cookieexists", "false")
				.data("submit", "Reboot")
				.post();
		logger.debug("channel {} rebooted", ch.getName());
		if (doc.html().contains("Please wait for a moment while rebooting"))
			return ChannelRebootTask.builder().id(ch.getId()).name(ch.getName()).success(true).taskDescr(td).build();
		else 
			return ChannelRebootTask.builder().id(ch.getId()).name(ch.getName()).success(false).taskDescr(td).build();
	}



	@Override
	public Class<?> getClazz() {
		return ChannelRebootTask.class;
	}
	

}
