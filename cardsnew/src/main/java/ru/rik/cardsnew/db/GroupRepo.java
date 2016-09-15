package ru.rik.cardsnew.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.GrpState;

@Repository
public class GroupRepo extends GenericRepoImpl<Grp, GrpState>  {
	static final Logger logger = LoggerFactory.getLogger(GroupRepo.class);

	private static final long serialVersionUID = 1L;


	public GroupRepo() {
		super(Grp.class, GrpState.class);	
	}
	
}
