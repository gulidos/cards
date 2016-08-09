package ru.rik.cardsnew.domain.repo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ru.rik.cardsnew.domain.ChannelState;

public class ChannelsStates  {
	ConcurrentMap<Long, ChannelState> channels;
	
	public ChannelsStates() {	
		channels = new ConcurrentHashMap<>();
	}
	
	/**  Returns:	the previous value associated with the specified key, 
	 * or null if there was no mapping for the key*/
	public ChannelState add(ChannelState c) {
		return channels.putIfAbsent(c.getTrunkId(), c);
	}
	
	
	public ChannelState findById (long id) {
		return channels.get(id);
	}
	
}
