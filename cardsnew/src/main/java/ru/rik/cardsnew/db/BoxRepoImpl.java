package ru.rik.cardsnew.db;

import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Box;

@Repository
public class BoxRepoImpl extends GenericRepoImpl<Box, Long>  {
	private static final long serialVersionUID = 1L;


	public BoxRepoImpl() {
		super(Box.class);	}
}
