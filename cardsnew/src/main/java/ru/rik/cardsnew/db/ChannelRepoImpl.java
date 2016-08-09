package ru.rik.cardsnew.db;

import static javax.persistence.LockModeType.NONE;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.repo.ChannelsStates;

@Repository
public class ChannelRepoImpl extends GenericRepoImpl<Channel, Long> {
	private static final long serialVersionUID = 1L;
	@Autowired
	private ChannelsStates channelsStates;
	
	public ChannelRepoImpl() {
		super(Channel.class);	
	}
	
	@Override
	public List<Channel> findAll() {
		List<Channel> channels = super.findAll();
		for (Channel ch : channels) {
			if (channelsStates.findById(ch.getId()) == null)
				channelsStates.add(new ChannelState(ch));
		}

		return channels;
	}
	
	 @Override
	    public Channel findById(Long id) {
		 Channel ch = findById(id, NONE);
		 if (channelsStates.findById(ch.getId()) == null)
				channelsStates.add(new ChannelState(ch));
	        return ch;
	    }
}
