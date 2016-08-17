package ru.rik.cardsnew.service.asterisk;

import java.util.Map;

import org.apache.log4j.Logger;

import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.CardStat;

public class CardStatHelper {
	static Logger log = Logger.getLogger(CardStatHelper.class);

	public CardStatHelper() {

	}

	public static int forAllStats(CdrExecutable command) {
		Map<String, Card> cards = Group.getAllcardsByName();
		int n = 0;
		for (String c : cards.keySet()) {
			try {
				Card card = cards.get(c);
				CardStat cStat = card.getCardStat();
				if (cStat != null) {
					command.execute(cStat);
					n++;
				}
			} catch (Exception e) {
				log.error(e, e);
			}
		}
		return n;
	}

	public static void refreshStats() {
		
		 class RefreshStatsCommand implements CdrExecutable {
				@Override
				public void execute(CardStat cardStat) {
					cardStat.calcAcd();
					cardStat.calcAsr();
				}
			}
		 
		 forAllStats(new RefreshStatsCommand());
	}
	

}
