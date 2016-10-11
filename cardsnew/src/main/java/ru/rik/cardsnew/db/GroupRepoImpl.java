package ru.rik.cardsnew.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.GrpState;

@Repository
public class GroupRepoImpl extends GenericRepoImpl<Grp, GrpState> implements GroupRepo  {
	static final Logger logger = LoggerFactory.getLogger(GroupRepoImpl.class);

	private static final long serialVersionUID = 1L;


	public GroupRepoImpl() {
		super(Grp.class, GrpState.class);	
	}
	
}
