package ru.rik.cardsnew.domain.repo;

import ru.rik.cardsnew.db.GenericRepo;
import ru.rik.cardsnew.domain.Trunk;

public class Trunks extends GenericMemImpl<Trunk> {
	
	public Trunks(GenericRepo<Trunk, Long> repo) {
		super(Trunk.class, repo);
	}
	

}
