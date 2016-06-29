package ru.rik.cardsnew.db;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Trunk;
import ru.rik.cardsnew.domain.TrunkState;
import ru.rik.cardsnew.domain.repo.TrunksStates;

@Repository
public class TrunkRepoImpl extends GenericRepoImpl<Trunk, Long> {
	private static final long serialVersionUID = 1L;

	@Autowired
	private TrunksStates trunksStats;

	public TrunkRepoImpl() {
		super(Trunk.class);
	}

	@Override
	public List<Trunk> findAll() {
		List<Trunk> trunks = super.findAll();
		for (Trunk t : trunks) {
			if (trunksStats.findById(t.getId()) == null)
				trunksStats.add(new TrunkState(t));
		}

		return trunks;
	}
}
