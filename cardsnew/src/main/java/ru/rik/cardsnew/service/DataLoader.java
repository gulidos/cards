package ru.rik.cardsnew.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.db.CardRepoImpl;
import ru.rik.cardsnew.db.ChannelRepoImpl;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.repo.Banks;
import ru.rik.cardsnew.domain.repo.Cards;
import ru.rik.cardsnew.domain.repo.Channels;
import ru.rik.cardsnew.domain.repo.Grps;

@EnableTransactionManagement
public class DataLoader {
	@Autowired Cards cards;
	@Autowired Channels channels;
	@Autowired Banks banks;
	@Autowired Grps grps;
	@Autowired CardRepoImpl cardRepo;
	@Autowired ChannelRepoImpl chRepo;

	
	
	
	public DataLoader() {
		
	}
	
	@Transactional
	public void init() {
		for (Card c : cardRepo.findAll()) {
			if (cards.add(c.getName(), c) != null) 
				throw new IllegalStateException("The card already axists " + c.toString());
			banks.add(c.getBank().getIp(), c.getBank());
			if (c.getGroup() != null )
				grps.add(c.getGroup().getName(), c.getGroup());
		}
		
		for (Channel ch : chRepo.findAll()) {
			if (channels.add(ch.getName(), ch) != null) throw new IllegalStateException("The channel already axists " + ch.toString());
			if (ch.getGroup() != null )
				grps.add(ch.getGroup().getName(), ch.getGroup());
		}
	}

}
