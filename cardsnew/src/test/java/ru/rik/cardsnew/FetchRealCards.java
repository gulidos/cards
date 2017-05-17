package ru.rik.cardsnew;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ru.rik.cardsnew.config.RootConfig;
import ru.rik.cardsnew.db.BankRepo;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Place;
import ru.rik.cardsnew.domain.State;
import ru.rik.cardsnew.service.TaskCompleter;
import ru.rik.cardsnew.service.TaskDescr;


public class FetchRealCards {

	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(RootConfig.class);
		Thread.sleep(40000);
		System.out.println("====================================  Context loaded =================");
		ChannelRepo chans= ctx.getBean(ChannelRepo.class);
		CardRepo cards = ctx.getBean(CardRepo.class);
		BankRepo banks = ctx.getBean(BankRepo.class);
		TaskCompleter taskCompleter = ctx.getBean(TaskCompleter.class);
		
		System.out.println("====================================  Cleaning cards=================");
		chans.setCardToNull(chans.findAll());
		System.out.println("====================================  Cleaning channels=================");
		cards.setChannelToNull(cards.findAll());
		
		System.out.println("====================================  Switching tasks begind=================");

		
		chans.findAll().stream().map(ch -> ch.getState(chans))
			.peek(st -> System.out.println("channel: " + st.getName()+ " "))
			.filter(st -> st.getSimset() != null)
//			.map(st -> st.getSimset())
			.forEach(st -> {
				Place place = Place.getInstance(st.getSimset().getCardPos());
				Bank bank = banks.findByName(st.getSimset().getBankIp()) ;
				Card c = cards.findCardsByPlace(place, bank);
				System.out.println("place: " + (place!=null ? place : null)+ " bank: " + (bank!=null?bank.getName():null) + 
						" card: " + (c != null ? c.getName() : null));
				try {
					Channel ch = chans.findById(st.getId());
					System.out.println("in channel " + (ch != null ? ch.getName() : null) + 
							" inst card: " + (c != null ? c.getName() : null));
					if (ch!= null)
						chans.switchCard(ch, c);
//						taskCompleter.addTask(() -> chans.switchCard(ch, c);, st);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

		Map<Future<State>, TaskDescr> map  = taskCompleter.getMap();
		for (int i = 0; i < 100; i++) {
			Thread.sleep(2000);
			System.out.println("");
			System.out.println("========================================== size: " + map.size());
			for (Future<State> s: map.keySet()) {
				TaskDescr descr = map.get(s);
				System.out.println(descr.getName() + " " + descr.getClazz().getSimpleName() + " " 
				+ (new Date().getTime() - descr.getLastChangeDate().getTime())/1000 +
						" " + descr.getStage() );
			}	

		}
	}
	
	public FetchRealCards() {	}

}
