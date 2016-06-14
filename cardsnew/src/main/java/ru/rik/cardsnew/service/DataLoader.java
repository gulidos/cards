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
import ru.rik.cardsnew.domain.repo.Trunks;

@EnableTransactionManagement
public class DataLoader {
	@Autowired Cards cards;
	@Autowired Channels channels;
	@Autowired Banks banks;
	@Autowired Grps grps;
	@Autowired Trunks trunks;
	@Autowired 
	CardRepoImpl cardRepo;
	@Autowired ChannelRepoImpl chRepo;

	
	
	
	public DataLoader() {
		System.out.println("!!! DataLoader constructor");
	}
	
	@Transactional
	public void init() {
		System.out.println("cardRepo: " + cardRepo);
		for (Card c : cardRepo.findAll()) {
			if (cards.add(c.getName(), c) != null) 
				throw new IllegalStateException("The card already axists " + c.toString());
		
			if (c.getGroup() != null )
				grps.add(c.getGroup().getName(), c.getGroup());
			
			banks.add(c.getBank().getIp(), c.getBank());
		}
		
		for (Channel ch : chRepo.findAll()) {
			if (channels.add(ch.getName(), ch) != null) 
				throw new IllegalStateException("The channel already axists " + ch.toString());
			
			if (ch.getGroup() != null )
				grps.add(ch.getGroup().getName(), ch.getGroup());
			
			if (ch.getTrunk() != null)
				trunks.add(ch.getTrunk().getName(), ch.getTrunk());
			
//			if (ch.getCard() != null ) 
				
			
		}
	}

}
