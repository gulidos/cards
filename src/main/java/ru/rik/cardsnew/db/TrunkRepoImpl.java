package ru.rik.cardsnew.db;

import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Trunk;
import ru.rik.cardsnew.domain.TrunkState;

@Repository
public class TrunkRepoImpl extends GenericRepoImpl<Trunk, TrunkState> implements TrunkRepo {


	public TrunkRepoImpl() {
		super(Trunk.class, TrunkState.class);
	}
}
