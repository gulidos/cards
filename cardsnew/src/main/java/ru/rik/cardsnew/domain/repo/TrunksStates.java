package ru.rik.cardsnew.domain.repo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.rik.cardsnew.domain.TrunkState;

public class TrunksStates  {
	ConcurrentMap<Long, TrunkState> trunks;
	private static final Logger logger = LoggerFactory.getLogger(TrunksStates.class);

	
	public TrunksStates() {	
		logger.debug("Instantiate the TrunkStats ...");
		trunks = new ConcurrentHashMap<>();
	}
	
	/**  Returns:	the previous value associated with the specified key, 
	 * or null if there was no mapping for the key*/
	public TrunkState add(TrunkState t) {
		return trunks.putIfAbsent(t.getTrunkId(), t);
	}
	
	
	public TrunkState findById (long id) {
		return trunks.get(id);
	}
	
}
