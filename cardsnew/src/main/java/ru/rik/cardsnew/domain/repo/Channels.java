package ru.rik.cardsnew.domain.repo;

import ru.rik.cardsnew.db.GenericRepo;
import ru.rik.cardsnew.domain.Channel;

public class Channels extends GenericMemImpl<Channel> {

	
	public Channels(GenericRepo<Channel, Long> repo) {
		super(Channel.class, repo);
	}
	
}
