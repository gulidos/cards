package ru.rik.cardsnew.web.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.db.GroupRepo;
import ru.rik.cardsnew.db.RoutingRepo;
import ru.rik.cardsnew.db.TrunkRepo;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Route;
import ru.rik.cardsnew.domain.Trunk;

@RestController
@RequestMapping("/rest")
@EnableTransactionManagement
public class RestTrunks {
	@Autowired GroupRepo groups;
	@Autowired CardRepo cards;
	@Autowired TrunkRepo trunks;
	@Autowired ChannelRepo chanRepo;
	@Autowired RoutingRepo routingRepo;

	public RestTrunks() {
	}

	
	@Transactional
	@RequestMapping(value = "/trunk/{name}/{number}", method = RequestMethod.GET)
	public RestTrunk get(@PathVariable("name") String name, 
			@PathVariable("number") String number) {

		Trunk t = trunks.findByName(name);
		if (t!= null) 
			return new RestTrunk(t, number);
		else return null;
	}
	
	@Transactional
	@RequestMapping(value = "/trunk/clear", method = RequestMethod.POST)
	public RestTrunk clear() {
		trunks.clearCache();
		return null;
	}

	@Data
	public class RestTrunk {
		String name;
		String number;
		List<RestChannel> channels = new ArrayList<>();

		public RestTrunk(Trunk t, String n) {
			name = t.getName(); 
			number = n;
			int i = 0;
			Route route = routingRepo.find(Long.valueOf(n));
			for (Channel ch : chanRepo.getSorted(t, number, route)) {
				RestChannel r = new RestChannel(i, ch.getName(), "P", 
						ch.getCard() != null ? ch.getCard().getName() : "", 
						ch.getCard() != null ? ch.getCard().getNumber() : "",
						ch.getBox().getIp(), 
						ch.getCard() != null ? ch.getCard().getDlimit() : 0,
						ch.getCard() != null ? ch.getCard().getMlimit() : 0, 
						ch.getCard() != null ? ch.getCard().getBank().getName() : "");
				channels.add(r);
				i++;
			}
		}
	}
	
	@Data @AllArgsConstructor 
	public class RestChannel {
		int id;
		String name;
		String type;
		String cardName;
		String cardNumber;
		String boxIp;
		int dayLimit;
		int monthLimit;
		String bankIp;
	}

}
