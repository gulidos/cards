package ru.rik.cardsnew.db;

import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Grp;

@Repository
public class GroupRepoImpl extends GenericRepoImpl<Grp, Long>  {
	private static final long serialVersionUID = 1L;


	public GroupRepoImpl() {
		super(Grp.class);	
	}
}
