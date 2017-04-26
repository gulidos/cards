package ru.rik.cardsnew.web;

import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.rik.cardsnew.domain.Card;

@EnableTransactionManagement
public abstract class GenericControllerImpl <T>{

	public GenericControllerImpl() {	}

	@RequestMapping(method = RequestMethod.GET)
	public String getList(Model model) {
//		model.addAttribute("cards", cards.findAll());
		
		if(! model.containsAttribute("card")) {
			Card card = new Card();
			model.addAttribute("card", card);
		}

		return "cards";
	}

}
