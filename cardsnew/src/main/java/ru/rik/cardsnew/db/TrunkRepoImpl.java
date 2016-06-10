package ru.rik.cardsnew.db;

import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Trunk;

@Repository
public class TrunkRepoImpl extends GenericRepoImpl<Trunk, Long>  {
	private static final long serialVersionUID = 1L;


	public TrunkRepoImpl() {
		super(Trunk.class);	}
}
