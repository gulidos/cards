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
import ru.rik.cardsnew.domain.MyState;
import ru.rik.cardsnew.domain.Sms;
import ru.rik.cardsnew.service.TaskDescr;

public class SmsTask implements MyState {
	private static final Logger logger = LoggerFactory.getLogger(SmsTask.class);		

	@Getter private final Channel ch;
	@Getter private final Card card;
	@Getter private final Channel pair;
	@Getter private final Card pairCard;
	@Getter private final TaskDescr td;
	@Getter @Setter private List<Sms> smslist;
	@Getter @Setter private List<Sms> pairSmslist;
	@Getter @Setter private TelnetClient telnetClient;
	@Getter @Setter private Phase phase; 
	
	
	
	@Builder
	public SmsTask(Channel ch, Card card, Channel pair, Card pairCard, 
			TelnetClient telnetClient, List<Sms> allsms, Phase phase, TaskDescr td) {
		if (ch == null || card == null)
			throw new IllegalArgumentException("channel or cards can not be null!");
		this.ch = ch;
		this.card = card;
		this.pairCard = pairCard;
		this.pair = pair;
		this.telnetClient = telnetClient;
		smslist = allsms;
		this.phase = phase;
		this.td = td;
	}
	

	public static SmsTask get(TelnetHelper h, Channel ch, Card card, Channel pair, Card pairCard, TaskDescr td) 
			throws SocketException, IOException {
		if (ch == null) throw new IllegalArgumentException("Channel can not be null");
		if (card == null) throw new IllegalArgumentException("Card can not be null");
	
		td.setStage("Connecting to" + ch.getBox().getIp() + ":" + ch.getLine().getTelnetport());	
		TelnetClient tc  = h.getConnection(ch.getBox().getIp() ,
				ch.getLine().getTelnetport(),
				Box.DEF_USER, Box.DEF_PASSWORD);
		
		List<Sms> allsms = h.FetchSmsFromChannel(tc, ch.getLine().getNport() + 1);
		td.setStage("Fetching main ");	
		SmsTask smstask = SmsTask.builder()
			.ch(ch).pair(pair)
			.card(card).pairCard(pairCard)
			.telnetClient(tc)
			.allsms(allsms)
			.phase(Phase.FetchMain)
			.td(td)
			.build();
		smstask.getSmslist().stream().forEach(s -> {s.setChannel(ch);	s.setCard(card);});
		return smstask;
	}

	
	public SmsTask deleteMain(TelnetHelper h) {
		phase = Phase.DeleteMain;
		td.setStage("Deleting main " + smslist.size() + " smses");	
		h.deleteSms(telnetClient, smslist);
		return this;
	}
	
	
	public SmsTask fetchPair(TelnetHelper h) {
		phase = Phase.FetchPair;
		if (pair != null) {
			td.setStage("Fetching pair");	
			pairSmslist = h.FetchSmsFromChannel(telnetClient, pair.getLine().getNport() + 1);
		}	
		pairSmslist.stream().forEach(s -> {s.setChannel(pair);	s.setCard(pairCard);});
		return this;
	}
	
	
	public SmsTask deletePair(TelnetHelper h){
		phase = Phase.DeletePair;
		if (pair != null)
			td.setStage("Deleting pair " + pairSmslist.size() + " smses");	
			h.deleteSms(telnetClient, pairSmslist);
		disconnect();
		return this;
	}
	
	public void disconnect() {
		try {
			td.setStage("Disconnecting");
			telnetClient.disconnect();
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	@Override public long getId() {return ch.getId();	}
	@Override public void setId(long id) {}

	@Override public String getName() {return ch.getName();}
	@Override public void setName(String name) {}

	@Override public Class<?> getClazz() {return SmsTask.class;}


	public enum Phase {
		FetchMain, DeleteMain, FetchPair, DeletePair; 
	}
}
