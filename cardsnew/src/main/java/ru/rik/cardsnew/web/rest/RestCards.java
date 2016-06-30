package ru.rik.cardsnew.web.rest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.GroupRepoImpl;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Oper;
import ru.rik.cardsnew.domain.Place;


//brew install httpie 
//http GET http://localhost:8080/cardsnew/rest/cards
@RestController 
@RequestMapping("/rest")
@EnableTransactionManagement
public class RestCards {
	@Autowired CardRepo cards;
	@Autowired GroupRepoImpl groups;
	
	public RestCards() {	}
	
	@Transactional
	@RequestMapping(value = "/cards", method=RequestMethod.GET) public List<RestCard> cards() {
		List<RestCard> lst = new ArrayList<>();
		for (Card c: cards.findAll()) {
			RestCard rc = new RestCard(c);
			lst.add(rc);
		}
		return lst; 
	}
	
	@Transactional
	@RequestMapping(value = "/card/{id}", method = RequestMethod.GET)
	public RestCard get(@PathVariable("id") long id) {
		Card t = cards.findById(id);
		if (t!= null) 
			return new RestCard(t);
		else return null;
	}
	@Data
	public class RestCard {
		long id;
		String name, number, sernumber, bank, group;
		Place place;
		Oper oper;
		
		public RestCard(Card c) {
			id = c.getId();
			name = c.getName();
			number = c.getNumber();
			sernumber = c.getSernumber();
			bank = (c.getBank() != null ? c.getBank().getName() : "");
			group = (c.getGroup() != null ? c.getGroup().getName() : "");
			place = c.getPlace();
			oper = c.getOper();	
		}
	}
	
	
	
	@Transactional
	@RequestMapping(value = "/groups", method=RequestMethod.GET) public List<RstGrp> groups() {
		List<RstGrp> lst = new ArrayList<>();
		for (Grp c: groups.findAll()) {
			if (c == null) 	break;
			
			RstGrp rc = new RstGrp(c);
			lst.add(rc);
		}
		return lst; 
	}
	
	@Data
	public class RstGrp {
		long id;
		String name;
		Set<String> cards = new HashSet<>();
		Set<String> channels = new HashSet<>();
		
		public RstGrp(Grp g) {
			id = g.getId();
			name = g.getName();
			for (Card c : g.getCards()) 
				if (c != null) 
					cards.add(c.getName());
			for (Channel c : g.getChannels()) 
				if (c != null) 
					channels.add(c.getName());
		}
	}
	
	
}
