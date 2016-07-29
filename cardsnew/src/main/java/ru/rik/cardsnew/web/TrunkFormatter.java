package ru.rik.cardsnew.web;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import ru.rik.cardsnew.domain.Trunk;

public class TrunkFormatter implements Formatter<Trunk> {

	
	public TrunkFormatter() {
		super();
	}

	@Override
	public String print(Trunk trunk, Locale locale) {
		System.out.println("!!! print id: " + trunk.getId());
		return String.valueOf(trunk.getId());
	}

	@Override
	public Trunk parse(String text, Locale locale) throws ParseException {
		System.out.println("!!! parse");
		long id = Long.parseLong(text);
		Trunk t = new Trunk();
		System.out.println("!!! parse id: " + id);

		return t;
	}

}
