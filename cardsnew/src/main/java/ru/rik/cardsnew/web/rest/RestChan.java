package ru.rik.cardsnew.web.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.service.SwitchTask;
import ru.rik.cardsnew.service.TaskCompleter;
import ru.rik.cardsnew.service.TaskDescr;


//brew install httpie 
//http GET http://localhost:8080/cardsnew/rest/cards
@RestController 
@RequestMapping("/rest")
@EnableTransactionManagement
public class RestChan {
	@Autowired CardRepo cards;
	@Autowired ChannelRepo chans;
	@Autowired SwitchTask switcher;
	@Autowired TaskCompleter taskCompleter;

	public RestChan() {	}
	
	@Transactional
	@RequestMapping(value = "/chan/sw/{name}", method = RequestMethod.POST)
	public String get(@PathVariable("name") String name) {
		Channel ch = chans.findByName(name);
		if (ch == null) 
			return "Channel with name " + name + " doesn't exist";
		
		Card c = cards.findTheBestInGroupForInsert(ch.getGroup());
		if (c == null)
			return "There aren't more cards for group " + ch.getGroup().getName() + " available";
		TaskDescr td = new TaskDescr(SwitchTask.class, ch.getState(chans), new Date());
		taskCompleter.addTask(() ->  switcher.switchCard(ch, c, td), td);
		return "Installing " + c.getName() + " card in " + ch.getName() +"  channel";
	}

	@Transactional
	@RequestMapping(value = "/chan/cards/{name}", method = RequestMethod.POST)
	public List<RestCard> getAllCards(@PathVariable("name") String name) {
		Channel ch = chans.findByName(name);
		if (ch == null) 
			throw new IllegalArgumentException("Channel with the name " + name + " doesn't exist");
		
		List<Card> list = cards.findAllAvailableForChannel(ch.getGroup());
		List<RestCard> result = new ArrayList<>();
		
		for (Card c: list)
			result.add(new RestCard(c));
		return result;
	}
	
	@Data
	public class RestCard {
		long id;
		String name, number, sernumber;
		int total;
		public RestCard(Card c) {
			id = c.getId();
			name = c.getName();
			number = c.getNumber();
			sernumber = c.getSernumber();
			total = c.getStat(cards).getMinRemains();
		}
	}
	
	
	}
