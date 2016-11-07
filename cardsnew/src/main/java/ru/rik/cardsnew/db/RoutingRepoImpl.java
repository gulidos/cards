package ru.rik.cardsnew.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ru.rik.cardsnew.domain.Oper;
import ru.rik.cardsnew.domain.Route;

public class RoutingRepoImpl implements RoutingRepo {
	static final Logger logger = LoggerFactory.getLogger(RoutingRepoImpl.class);
	static final String query = "select * from numberplan";
	private volatile TreeMap<Long, Route> map = new TreeMap<>();
	
	public RoutingRepoImpl() {
	}
	
	@Autowired
	@Override
	public int load(DataSource ds) {
		int n = 0;
        TreeMap<Long, Route> newMap = new TreeMap<>();

		try (Connection con = ds.getConnection(); PreparedStatement stmt = con.prepareStatement(query)) {
			long start = System.currentTimeMillis();
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Route r = Route.builder()
						.fromd(rs.getLong("fromd"))
						.tod(rs.getLong("tod"))
						.oper(Oper.findByMnc(rs.getInt("owner")))
						.regcode(rs.getInt("regcode"))
						.build();
				newMap.put(r.getFromd(), r);
				n++;
			}
			map = newMap; 
			long duration = System.currentTimeMillis() - start;
			logger.info("duration:" + duration + "ms, cache size:" + newMap.size());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return n;
	}	
	
	
	@Override
    public Route find(Long num) {
        Entry<Long, Route> closestEntry = map.floorEntry(num);
        if (closestEntry != null) {
        	Route r = closestEntry.getValue();
            if (r != null && r.isIn(num)) 
                return r;
        }
        return Route.NULL_ROUTE;
    }


}
