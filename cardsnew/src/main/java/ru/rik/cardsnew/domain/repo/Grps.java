package ru.rik.cardsnew.domain.repo;

import ru.rik.cardsnew.db.GenericRepo;
import ru.rik.cardsnew.domain.Grp;

public class Grps extends GenericMemImpl<Grp> {
	
	public Grps(GenericRepo<Grp, Long> repo) {
		super(Grp.class, repo);
	}
	

}
