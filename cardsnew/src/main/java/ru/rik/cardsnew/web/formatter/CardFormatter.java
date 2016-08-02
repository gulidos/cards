package ru.rik.cardsnew.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import ru.rik.cardsnew.db.CardRepoImpl;
import ru.rik.cardsnew.domain.Card;

public class CardFormatter implements Formatter<Card>{

	@Autowired CardRepoImpl cacrds;
	
	public CardFormatter()  {
		super();
	}

	@Override
	public String print(Card c, Locale locale) {
		return (c != null ? String.valueOf(c.getId()) : "");
	}

	@Override
	public Card parse(String str, Locale locale) throws ParseException {
		Long id = Long.parseLong(str);
		return cacrds.findById(id);
	}

}
