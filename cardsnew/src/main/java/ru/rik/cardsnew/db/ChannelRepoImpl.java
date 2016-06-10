package ru.rik.cardsnew.db;

import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Channel;

@Repository
public class ChannelRepoImpl extends GenericRepoImpl<Channel, Long> {
	private static final long serialVersionUID = 1L;

	
	public ChannelRepoImpl() {
		super(Channel.class);	}
}
