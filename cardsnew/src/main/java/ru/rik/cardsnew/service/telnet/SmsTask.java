package ru.rik.cardsnew.service.telnet;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;
import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Line;
import ru.rik.cardsnew.domain.MyState;
import ru.rik.cardsnew.domain.Sms;

public class SmsTask implements MyState {
	private static final Logger logger = LoggerFactory.getLogger(SmsTask.class);		

	@Getter private final Channel ch;
	@Getter private final Card card;
	@Getter private final Channel pair;
	@Getter private final Card pairCard;
	@Getter private List<Sms> smslist;
	@Getter @Setter private TelnetClient telnetClient;
	@Getter @Setter private Phase phase; 
	
	
	@Builder
	public SmsTask(Channel ch, Card card, Channel pair, Card pairCard, TelnetClient telnetClient, List<Sms> allsms, Phase phase) {
		super();
		this.ch = ch;
		this.card = card;
		this.pairCard = pairCard;
		this.pair = pair;
		this.telnetClient = telnetClient;
		smslist = allsms;
		this.phase = phase;
	}
	

	public static SmsTask get(TelnetHelper h, Channel ch, Card card, Channel pair, Card pairCard) throws SocketException, IOException {
		TelnetClient tc  = h.getConnection(ch.getBox().getIp() ,
				ch.getLine().getTelnetport(),
				Box.DEF_USER, Box.DEF_PASSWORD);
		
		List<Sms> allsms = h.FetchSmsFromChannel(tc, ch.getLine().getNport() + 1);
		System.out.println("FetchMain " + ch.getName());
		SmsTask smstask = SmsTask.builder()
			.ch(ch).pair(pair)
			.card(card).pairCard(pairCard)
			.telnetClient(tc)
			.allsms(allsms)
			.phase(Phase.FetchMain)
			.build();
		
		return smstask;
	}

	
	public SmsTask deleteMain(TelnetHelper h){
		phase = Phase.DeleteMain;
		System.out.println(phase + " " + ch.getName());
		h.deleteSms(telnetClient, smslist);
		return this;
	}
	
	
	public SmsTask fetchPair(TelnetHelper h) {
		phase = Phase.FetchPair;
		System.out.println(phase + " " + ch.getName());
		if (pair != null)
			smslist = h.FetchSmsFromChannel(telnetClient, pair.getLine().getNport() + 1);
		return this;
	}
	
	
	public SmsTask deletePair(TelnetHelper h){
		phase = Phase.DeletePair;
		System.out.println(phase + " " + ch.getName());
		if (pair != null)
			h.deleteSms(telnetClient, smslist);
		try {
			getTelnetClient().disconnect();
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		return this;
	}
	
	
	@Override public long getId() {return ch.getId();	}
	@Override public void setId(long id) {}

	@Override public String getName() {return ch.getName();}
	@Override public void setName(String name) {}

	@Override public Class<?> getClazz() {return SmsTask.class;}


	public enum Phase {
		FetchMain, DeleteMain, FetchPair, DeletePair; 
	}
	

	public static void main(String[] args) throws SocketException, IOException {
		TelnetHelper h = new TelnetHelper();
		Channel ch = Channel.builder()
				.box(Box.builder().ip("172.17.1.34").build())
				.line(Line.L6)
				.build();
		
		
		Channel pair = Channel.builder()
				.box(Box.builder().ip("172.17.1.34").build())
				.line(Line.L5)
				.build();
		
		SmsTask task = get(h, ch, null, pair, null);
		System.out.println(task.getSmslist());
		if (task.phase == Phase.FetchMain)
			task = task.deleteMain(h);
		
		if (task.phase == Phase.DeleteMain)
			task.fetchPair(h);
		
		if (task.phase == Phase.DeletePair)
			task.deletePair(h);
		
		System.out.println(task.getSmslist());

		
		

	}
}
