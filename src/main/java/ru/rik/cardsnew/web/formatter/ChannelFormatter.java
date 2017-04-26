package ru.rik.cardsnew.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.domain.Channel;

public class ChannelFormatter implements Formatter<Channel>{

	@Autowired ChannelRepo channels;
	
	public ChannelFormatter()  {
		super();
	}

	@Override
	public String print(Channel ch, Locale locale) {
		return (ch != null ? String.valueOf(ch.getId()) : "");
	}

	@Override
	public Channel parse(String str, Locale locale) throws ParseException {
		Long id = Long.parseLong(str);
		return channels.findById(id);
	}

}
