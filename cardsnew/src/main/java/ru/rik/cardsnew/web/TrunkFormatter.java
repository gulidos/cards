package ru.rik.cardsnew.web;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import ru.rik.cardsnew.db.TrunkRepoImpl;
import ru.rik.cardsnew.domain.Trunk;

public class TrunkFormatter implements Formatter<Trunk> {
	@Autowired TrunkRepoImpl trunks;
	
	
	public TrunkFormatter() {
		super();
	}

	@Override
	public String print(Trunk trunk, Locale locale) {
		System.out.println("!!! print id: " + trunk.getId());
		return String.valueOf(trunk.getId());
	}
	

	@Override
	public Trunk parse(String id, Locale locale) throws ParseException {
		Long trunkId = Long.parseLong(id);
		Trunk t = trunks.findById(trunkId);
		return t;
	}

}
