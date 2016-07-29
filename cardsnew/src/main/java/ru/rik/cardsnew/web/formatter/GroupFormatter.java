package ru.rik.cardsnew.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import ru.rik.cardsnew.db.GroupRepoImpl;
import ru.rik.cardsnew.domain.Grp;

public class GroupFormatter implements Formatter<Grp>{

	@Autowired GroupRepoImpl grps;
	
	public GroupFormatter()  {
		super();
	}

	@Override
	public String print(Grp grp, Locale locale) {
		return (grp != null ? String.valueOf(grp.getId()) : "");
	}

	@Override
	public Grp parse(String str, Locale locale) throws ParseException {
		Long id = Long.parseLong(str);
		return grps.findById(id);
	}

}
