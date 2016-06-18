package ru.rik.cardsnew.service;

import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
public class DataLoader {
//
//	
//	@Autowired 	CardRepoImpl cardRepo;
//	@Autowired ChannelRepoImpl chRepo;
//	@Autowired 	GroupRepoImpl grRepo;
//	
//	
//	
//	public DataLoader() {
//		System.out.println("!!! DataLoader constructor");
//	}
//	
//	@Transactional
//	public void init() {
//		System.out.println("cardRepo: " + cardRepo);
//		for (Card c : cardRepo.findAll()) {
//			if (cards.add(c.getId(), c) != null) 
//				throw new IllegalStateException("The card already axists " + c.toString());
//		
//			if (c.getGroup() != null ) {
//				grps.add(c.getGroup().getId(), c.getGroup());
//			}	
//			
//			if (c.getBank() != null)
//				banks.add(c.getBank().getId(), c.getBank());
//		}
//		
//		for (Channel ch : chRepo.findAll()) {
//			if (channels.add(ch.getId(), ch) != null) 
//				throw new IllegalStateException("The channel already axists " + ch.toString());
//			
//			if (ch.getGroup() != null ) {
//				ch.getGroup().hashCode();
//				grps.add(ch.getGroup().getId(), ch.getGroup());
//			}	
//			
//			if (ch.getTrunk() != null)
//				trunks.add(ch.getTrunk().getId(), ch.getTrunk());
//			
////			if (ch.getCard() != null ) 	
//		}
//		
//		for (Grp gr: grRepo.findAll()) {
//			if (gr != null ) {
//				grps.add(gr.getId(), gr);
//			}
//		}
//	}
//	
//	public void clearData() {
//		cards.clear();
//		channels.clear();
//		banks.clear();
//		grps.clear();
//		trunks.clear();
//	}

}
