package ru.rik.cardsnew.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import ru.rik.cardsnew.db.CardRepoImpl;
import ru.rik.cardsnew.db.ChannelRepoImpl;
import ru.rik.cardsnew.db.GroupRepoImpl;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Grp;
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
	
	@Autowired 	CardRepoImpl cardRepo;
	@Autowired ChannelRepoImpl chRepo;
	@Autowired 	GroupRepoImpl grRepo;
	
	
	
	public DataLoader() {
		System.out.println("!!! DataLoader constructor");
	}
	
	@Transactional
	public void init() {
		System.out.println("cardRepo: " + cardRepo);
		for (Card c : cardRepo.findAll()) {
			if (cards.add(c.getId(), c) != null) 
				throw new IllegalStateException("The card already axists " + c.toString());
		
			if (c.getGroup() != null ) {
				grps.add(c.getGroup().getId(), c.getGroup());
			}	
			
			if (c.getBank() != null)
				banks.add(c.getBank().getId(), c.getBank());
		}
		
		for (Channel ch : chRepo.findAll()) {
			if (channels.add(ch.getId(), ch) != null) 
				throw new IllegalStateException("The channel already axists " + ch.toString());
			
			if (ch.getGroup() != null ) {
				ch.getGroup().hashCode();
				grps.add(ch.getGroup().getId(), ch.getGroup());
			}	
			
			if (ch.getTrunk() != null)
				trunks.add(ch.getTrunk().getId(), ch.getTrunk());
			
//			if (ch.getCard() != null ) 	
		}
		
		for (Grp gr: grRepo.findAll()) {
			if (gr != null ) {
				grps.add(gr.getId(), gr);
			}
		}
	}
	
	public void clearData() {
		cards.clear();
		channels.clear();
		banks.clear();
		grps.clear();
		trunks.clear();
	}

}
