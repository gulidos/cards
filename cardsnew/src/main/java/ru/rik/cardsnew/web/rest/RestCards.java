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

import lombok.Data;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.db.GroupRepo;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Oper;
import ru.rik.cardsnew.domain.Place;


//brew install httpie 
//http GET http://localhost:8080/cardsnew/rest/cards
@RestController 
@RequestMapping("/rest")
@EnableTransactionManagement
public class RestCards {
	@Autowired CardRepo cards;
	@Autowired ChannelRepo channels;
	@Autowired GroupRepo groups;
	
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
//			bank = (c.getBank() != null ? c.getBank().getName() : "");
//			group = (c.getGroup() != null ? c.getGroup().getName() : "");
//			place = c.getPlace();
//			oper = c.getOper();	
		}
	}
	
	
	}
