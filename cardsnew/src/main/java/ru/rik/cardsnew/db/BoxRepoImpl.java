package ru.rik.cardsnew.db;

import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.BoxStat;

@Repository
public class BoxRepoImpl extends GenericRepoImpl<Box, BoxStat> implements BoxRepo  {
	private static final long serialVersionUID = 1L;


	public BoxRepoImpl() {
		super(Box.class, BoxStat.class);	
	}
}
