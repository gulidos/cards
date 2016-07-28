package ru.rik.cardsnew.web;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import ru.rik.cardsnew.domain.Trunk;

public class TrunkFormatter implements Formatter<Trunk> {

	public TrunkFormatter() {
	}

	@Override
	public String print(Trunk trunk, Locale locale) {
		return String.valueOf(trunk.getId());
	}

	@Override
	public Trunk parse(String id, Locale locale) throws ParseException {
		Trunk t = new Trunk();
		t.setId(Long.parseLong(id));
		return t;
	}

}
