package ru.rik.cardsnew.service.asterisk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ru.rik.cardsnew.db.CardRepoImpl;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.CardStat;
import ru.rik.cardsnew.domain.events.CdrCardEvent;
import ru.rik.cardsnew.domain.repo.CardsStates;


public class CheckCDRTask {
	static final Logger logger = LoggerFactory.getLogger(CheckCDRTask.class);

	@Autowired private CardsStates cardsStates;
	@Autowired private CardRepoImpl cardRepo;
	@Autowired private DataSource ds;

	public CheckCDRTask() {
		logger.info("Creating Tast for getting CDRs");
	}

	public void init() {
		logger.info("CheckCDRTask initing");
		getCDR(5760, true);
		cardsStates.refreshStats();
		
		//logStat();
	}
	
	public void getCDR(int min, boolean isInit)  {
		int n = 0;
		String query = "select * from cdr where calldate between date_sub(now(), INTERVAL ? minute) and NOW()"
				+ " and userfield <> \"\" order by calldate";
		
		try (Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, min);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String cardname = rs.getString("userfield");
				if (cardname == null) continue;
				
				Card card = cardRepo.findByName(cardname);
				if (card != null) {
					try {
						CdrCardEvent cdr = CdrCardEvent.builder()
								.date(rs.getString("calldate"))
								.src(rs.getString("src"))
								.dst(rs.getString("dst"))
								.cardId(card.getId())
								.billsec(rs.getInt("billsec"))
								.trunk(rs.getString("trunk"))
								.disp(rs.getString("disposition"))
								.regcode(rs.getString("regcode"))
								.build();
						CardStat cardStat = cardsStates.findById(card.getId());
						if (cardStat == null) {
							cardStat = new CardStat(card);
							cardsStates.add(cardStat);
						}	
						cardStat.addEvent(cdr);
						n++;
					} catch (ParseException pe) {
						logger.error("can not create CdrEvent calldate: " + rs.getString("calldate") +" cardname: " + cardname, pe);
					}
					
				} else {
					//log.error("can not find a Card with name " + cardname);
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
		logger.debug(n + " CDRs loaded");
	}

//	public static void logStat() {
//		Map<String, Card> allcards = Group.getAllcardsByName();
//		for (String c : allcards.keySet()) {
//			Card card = allcards.get(c);
//			CardStat cardStat = card.getCardStat();
//			if (cardStat != null) {
//				log.info(cardStat.toString());
//			}	
//		}
//	}

}
