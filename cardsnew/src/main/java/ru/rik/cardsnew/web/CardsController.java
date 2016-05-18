package ru.rik.cardsnew.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.rik.cardsnew.db.CardsRepository;
import ru.rik.cardsnew.domain.Card;

@Controller
@RequestMapping("/cards")
public class CardsController {
	private CardsRepository cardsRepository;

	@Autowired
	public CardsController(CardsRepository cardsRepository) {
		this.cardsRepository = cardsRepository;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String spittles(Model model) {
		model.addAttribute("cards", cardsRepository.findAll());
		
		if(! model.containsAttribute("cardForm")) {
			CardForm cardForm = new CardForm();
			model.addAttribute("cardForm", cardForm);
		}

		return "cards";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String saveCard(@Valid @ModelAttribute CardForm cardForm, 
			BindingResult errors,
			Model model, 
			RedirectAttributes redirectAttrs) {
		
		if (errors.hasErrors()) {
			System.out.println("there are validation errors");
			redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.cardForm", errors);
			redirectAttrs.addFlashAttribute("cardForm", cardForm);
			return "redirect:/cards";
		} else {
			cardsRepository.save(new Card(cardForm));
			String message = "Strategy " + cardForm.getId() + " was successfully added";
			model.addAttribute("message", message);
			return "redirect:/cards";
		}
	}
	
	@RequestMapping(value="/edit", method=RequestMethod.GET)
	public String  editCardPage(@RequestParam(value="id", required=true) Integer id, Model model) {
		
		if(! model.containsAttribute("card")) {
			System.out.println("Reading a card for a first time");
			Card card = cardsRepository.findOne(id);
			CardForm cardForm = new CardForm(card);
			model.addAttribute("card", cardForm);
		}
		return "card-edit";
	}
	
	@RequestMapping(value="/edit", method=RequestMethod.POST)
	public String editCard(
			@Valid @ModelAttribute CardForm cardForm, 
			BindingResult result,
			Model model, 
			RedirectAttributes redirectAttrs,
			@RequestParam(value="action", required=true) String action ) {
		
		System.out.println("action: " + action );
		if (action.equals("cancel")) {
			String message = "Card " + cardForm.getId() + " edit cancelled";
			model.addAttribute("message", message);
		} else if (result.hasErrors()) {
			System.out.println("there are validation errors");
			redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.card", result);
			redirectAttrs.addFlashAttribute("card", cardForm);
			return "redirect:/cards/edit?id=" + cardForm.getId();
		} else if (action.equals("save")) {
			System.out.println("Card "+ cardForm.getId() + " was successfully edited");
			cardsRepository.update(cardForm);
			String message = "Card " + cardForm.getId() + " was successfully edited";
			model.addAttribute("message", message);
		} 

		return "redirect:/cards";		
	}
	

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String showSpitterProfile(Model model,
			@RequestParam(value = "id", required = true) long id,
			@RequestParam(value="phase", required=true) String phase) {
		
		Card card = cardsRepository.findOne(id);
		System.out.println("card/delete-GET | id = " + id + " | phase = " + phase + " | " + card.toString());
		
		String view = null;
		if (phase.equals("stage")) {
			view = "card-delete";
			CardForm cardForm = new CardForm(card);
			model.addAttribute("cardForm", cardForm);
			String message = "card " + card.getId() + " queued for display.";
			model.addAttribute("message", message);
		}
		
		if (phase.equals("confirm")) {
			view ="redirect:/cards";
			cardsRepository.delete(card);
			String message = "Strategy " + card.getId() + " was successfully deleted";
			model.addAttribute("message", message);
		}
		
		if (phase.equals("cancel")) {
			view ="redirect:/cards";
			String message = "Strategy delete was cancelled.";
			model.addAttribute("message", message);
		}
		
		return view;
	}
}
