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

import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Util;
import ru.rik.cardsnew.domain.events.Cdr;
import ru.rik.cardsnew.domain.repo.CardsStates;
import ru.rik.cardsnew.domain.repo.Cdrs;


public class CheckCDRTask {
	static final Logger logger = LoggerFactory.getLogger(CheckCDRTask.class);

	@Autowired private CardsStates cardsStates;
	@Autowired private CardRepo cardRepo;
	@Autowired private ChannelRepo chanRepo;
	@Autowired private DataSource ds;
	@Autowired private Cdrs cdrs;

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
				String destchanname = rs.getString("dstchannel");
				if (cardname == null ) continue; 
				Card card = cardRepo.findByName(cardname);
				Channel chan = chanRepo.findByName(Cdr.parseChannel(destchanname));
				
				if (card != null && chan != null) { 
					try {
//						logger.debug("== our card: " + card.toString());

						Cdr cdr = Cdr.builder()
								.date(Util.parseDate(rs.getString("calldate"), "yyyy-MM-dd HH:mm:ss.S"))
								.src(rs.getString("src"))
								.dst(rs.getString("dst"))
								.cardId(card.getId())
								.billsec(rs.getInt("billsec"))
								.trunk(rs.getString("trunk"))
								.disp(rs.getString("disposition"))
								.regcode(rs.getString("regcode"))
								.uniqueid(rs.getString("uniqueid"))
								.channelId(chan != null ? chan.getId() : 0)
								.build();
						card.getStat().applyCdr(cdr, card);

						
						n++;
					} catch (ParseException pe) {
						logger.error("can not create CdrEvent calldate: " + rs.getString("calldate") +" cardname: " + cardname, pe);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
					
				} else {
//					logger.error("can not find a Card with name " + cardname);
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
		logger.debug(n + " CDRs loaded");
	}


}
