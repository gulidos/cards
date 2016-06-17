package ru.rik.cardsnew.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Oper;
import ru.rik.cardsnew.domain.Place;
import ru.rik.cardsnew.domain.repo.Cards;
import ru.rik.cardsnew.domain.repo.Grps;
import ru.rik.cardsnew.service.DataLoader;

@Controller
@RequestMapping("/cards")
@EnableTransactionManagement
public class CardsController {
	private Cards cards;

	@Autowired
	public CardsController(Cards cards) {
		this.cards = cards;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String spittles(Model model) {
		model.addAttribute("cards", cards.getMap().values());
		
		if(! model.containsAttribute("card")) {
			Card card = new Card();
			model.addAttribute("card", card);
		}

		return "cards";
	}

	@RequestMapping(value="/add", method=RequestMethod.GET)
	public String  addCard(Model model) {
		Card card = new Card();
		model.addAttribute("card", card);
		return "card-edit";
	}
	
//	@RequestMapping(value = "/add", method = RequestMethod.POST)
//	public String saveCard(@Valid @ModelAttribute Card card, 
//			BindingResult errors,
//			Model model, 
//			RedirectAttributes redirectAttrs) {
//		
//		if (errors.hasErrors()) {
//			System.out.println("there are validation errors");
//			redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.card", errors);
//			redirectAttrs.addFlashAttribute("card", card);
//			return "redirect:/cards";
//		} else {
//			cards.makePersistent(card);
//			String message = "Strategy " + card.getId() + " was successfully added";
//			model.addAttribute("message", message);
//			return "redirect:/cards";
//		}
//	}
	@Autowired Grps groups;
	@Transactional
	@RequestMapping(value="/edit", method=RequestMethod.GET)
	public String  editCardPage(@RequestParam(value="id", required=true) long id, Model model) {
		
		if(! model.containsAttribute("card")) {
			System.out.println("Reading a card for a first time");
			Card card = cards.findById(id);
			model.addAttribute("card", card);
			model.addAttribute("opers", Oper.values());
			model.addAttribute("places", Place.values());
			model.addAttribute("groups", groups.getMap().values());
		}
		return "card-edit";
	}
	
	@Transactional
	@RequestMapping(value="/edit", method=RequestMethod.POST)
	public String editCard(
			@Valid @ModelAttribute Card card, 
			BindingResult result,
			Model model, 
			RedirectAttributes redirectAttrs,
			@RequestParam(value="action", required=true) String action ) {
		
		System.out.println("action: " + action + " card: " + card.toStringAll());
		if (action.equals("cancel")) {
			String message = "Card " + card.toString() + " edit cancelled";
			model.addAttribute("message", message);
		} else if (result.hasErrors()) {
			System.out.println("there are validation errors" + result.getAllErrors().toString());
			redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.card", result);
			redirectAttrs.addFlashAttribute("card", card);
			return "redirect:/cards/edit?id=" + card.getId();
		} else if (action.equals("save") && card != null) {
//			System.out.println("Card "+ card.toString() + " was successfully edited");
//			Card existingCard = cards.findById(card.getId()); 
			
			cards.update(card);
			String message = "Card " + card.getId() + " was successfully edited";
			model.addAttribute("message", message);
		} 

		return "redirect:/cards";		
	}
	
	@Autowired
	DataLoader dataLoader;
	@RequestMapping(value = "/reload", method = RequestMethod.GET)
	public String reloadSetings(Model model,
			RedirectAttributes redirectAttrs,
			@RequestParam(value="phase", required=true) String phase) {

		if (phase.equals("reload")) {
			dataLoader.clearData();
			dataLoader.init();
		} 
		
		return "redirect:/";
	}
	
//	@Transactional
//	@RequestMapping(value = "/delete", method = RequestMethod.GET)
//	public String showSpitterProfile(Model model,
//			@RequestParam(value = "id", required = true) long id,
//			@RequestParam(value="phase", required=true) String phase) {
//		
//		Card card = cards.findById(id);
//		System.out.println("card/delete-GET | id = " + id + " | phase = " + phase + " | " + card.toString());
//		
//		String view = null;
//		if (phase.equals("stage")) {
//			view = "card-delete";
////			Card card= new Card();
////			model.addAttribute("cardForm", cardForm);
//			String message = "card " + card.getId() + " queued for display.";
//			model.addAttribute("message", message);
//		}
//		
//		if (phase.equals("confirm")) {
//			view ="redirect:/cards";
//			cards.makeTransient(card);
//			String message = "Strategy " + card.getId() + " was successfully deleted";
//			model.addAttribute("message", message);
//		}
//		
//		if (phase.equals("cancel")) {
//			view ="redirect:/cards";
//			String message = "Strategy delete was cancelled.";
//			model.addAttribute("message", message);
//		}
//		
//		return view;
//	}
}
