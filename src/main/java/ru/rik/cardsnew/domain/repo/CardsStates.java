package ru.rik.cardsnew.domain.repo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import ru.rik.cardsnew.domain.CardStat;
import ru.rik.cardsnew.domain.events.Event.Executable;

public class CardsStates  {
	static Logger logger = Logger.getLogger(CardsStates.class);

	ConcurrentMap<Long, CardStat> cards;
	
	public CardsStates() {	
		cards = new ConcurrentHashMap<>();
	}
	
	
	

	public Map<Long, CardStat> findAll(){
		return cards;
	}
	
	public int forAllStats(Executable command) {
		int n = 0;
		for (CardStat state : findAll().values()) {
			try {
				if (state != null) {
					command.execute(state);
					n++;
				}
			} catch (Exception e) {
				logger.error(e, e);
			}
		}
		return n;
	}

	public void refreshStats() {
		
		 class RefreshStatsCommand implements Executable {
				@Override
				public void execute(CardStat cardStat) {
//					cardStat.calcAcd();
//					cardStat.calcAsr();
				}
			}
		 
		 forAllStats(new RefreshStatsCommand());
	}
	
}
