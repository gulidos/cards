package ru.rik.cardsnew.db;

import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.GrpState;

@Repository
public class GroupRepoImpl extends GenericRepoImpl<Grp, GrpState>  {
	private static final long serialVersionUID = 1L;


	public GroupRepoImpl() {
		super(Grp.class, GrpState.class);	
	}
}
