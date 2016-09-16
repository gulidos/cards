package ru.rik.cardsnew.web;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.rik.cardsnew.db.BankRepoImpl;
import ru.rik.cardsnew.db.CardRepoImpl;
import ru.rik.cardsnew.db.ChannelRepoImpl;
import ru.rik.cardsnew.db.GroupRepo;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Oper;
import ru.rik.cardsnew.domain.Place;

@Controller
@RequestMapping("/cards")
@EnableTransactionManagement
@SessionAttributes("fc") 
public class CardsController {
	private static final Logger logger = LoggerFactory.getLogger(CardsController.class);

	@Autowired GroupRepo groups;
	@Autowired BankRepoImpl banks;
	@Autowired CardRepoImpl cards;
	@Autowired ChannelRepoImpl channels;
	
	
	public CardsController() {
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getList(Model model, @ModelAttribute("fc") Filter fc) {
		if (fc == null) {
			fc = new Filter();
			model.addAttribute("fc", fc);
		}
		
		fc.setGroupId(0);
		
		model.addAttribute("cards", cards.findAll());
		
		
		if (!model.containsAttribute("card")) {
			Card card = new Card();
			model.addAttribute("card", card);
		}

		return "cards";
	}
	
	@RequestMapping(value = "/group", method = RequestMethod.GET)
	public String getListGroup(@RequestParam(value = "id", required = true) long id, Model model) {
		Grp grp = groups.findById(id);
		
		if (grp != null) 
			model.addAttribute("cards", cards.findGroupCards(grp));
		
		return "cards";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addCard(Model model) {
		Card card = new Card();
		addToModel(model, card);
		return "card-edit";
	}

	@Transactional
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String editCardPage(@RequestParam(value = "id", required = true) long id, Model model) {

		if (!model.containsAttribute("card")) {
			Card card = cards.findById(id);
			addToModel(model, card);
			System.out.println("Reading a card for editing " + card.toStringAll());

		}
		return "card-edit";
	}

	private void addToModel(Model model, Card card) {
		model.addAttribute("card", card);
		model.addAttribute("places", Place.values());
		model.addAttribute("groups", groups.findAll());
		model.addAttribute("banklst", banks.findAll());
		model.addAttribute("channels", channels.findAll());
	}

	@Transactional
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editCard(@Valid @ModelAttribute Card card, BindingResult result, Model model,
			RedirectAttributes redirectAttrs, @RequestParam(value = "action", required = true) String action) {

		System.out.println("action: " + action + " card: " + card.toStringAll());
		if (action.equals("cancel")) {
			String message = "Card " + card.toString() + " edit cancelled";
			model.addAttribute("message", message);

		} else if (result.hasErrors()) {
			System.out.println("there are validation errors" + result.getAllErrors().toString());
			redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.card", result);
			redirectAttrs.addFlashAttribute("card", card);
			redirectAttrs.addFlashAttribute("opers", Oper.values());
			redirectAttrs.addFlashAttribute("places", Place.values());
			redirectAttrs.addFlashAttribute("groups", groups.findAll());
			redirectAttrs.addFlashAttribute("banklst", banks.findAll());
			return "redirect:/cards/edit?id=" + card.getId();

		} else if (action.equals("save") && card != null) {

			cards.makePersistent(card);
			String message = "Card " + card.getId() + " was successfully edited";
			model.addAttribute("message", message);
		}
//		if (fc.getGroupId()!= 0)
//			return "redirect:/cards/group?id=" + fc.getGroupId();

		return "redirect:/cards";
	}

	@Transactional
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String showSpitterProfile(Model model, @RequestParam(value = "id", required = true) long id,
			@RequestParam(value = "phase", required = true) String phase) {
		System.out.println("we are about to delete cardId: " + id);
		Card card = cards.findById(id);
		if (card != null)
			System.out.println(card.toString());
		else
			System.out.println("!!! Card not found");
		// System.out.println("card/delete-GET | id = " + id + " | phase = " +
		// phase + " | " + card.toString());

		String view = null;
		if (phase.equals("stage")) {
			model.addAttribute("opers", Oper.values());
			model.addAttribute("places", Place.values());
			model.addAttribute("groups", groups.findAll());
			model.addAttribute("banklst", banks.findAll());
			model.addAttribute("card", card);
			String message = "card " + card.getId() + " queued for display.";
			model.addAttribute("message", message);
			model.addAttribute("action", "delete");
			view = "card-edit";
		}

		if (phase.equals("confirm")) {
			view = "redirect:/cards";
			cards.makeTransient(card);
			String message = "Strategy " + card.getId() + " was successfully deleted";
			model.addAttribute("message", message);
		}

		if (phase.equals("cancel")) {
			view = "redirect:/cards";
			String message = "Strategy delete was cancelled.";
			model.addAttribute("message", message);
		}

		return view;
	}

//	@ExceptionHandler({ DataIntegrityViolationException.class, ConstraintViolationException.class,
//			PersistenceException.class })
//	public ModelAndView dataIntegrityError(HttpServletRequest req, Exception ex) {
//		logger.error("Request: " + req.toString() + " raised " + ex, ex);
//		ModelAndView mav = new ModelAndView();
//		Model model = (Model) mav.getModel();
//		model.addAttribute("exception", ex);
//		model.addAttribute("url", req.getRequestURL());
//		model.addAttribute("url", req.getRequestURL());
//		model.addAttribute("opers", Oper.values());
//		model.addAttribute("places", Place.values());
//		model.addAttribute("groups", groups.findAll());
//		model.addAttribute("banklst", banks.findAll());
//				
//		
//		mav.setViewName("redirect:/cards/edit?id=");
//		
//		
//		return mav;
//	}

	
	@RequestMapping(value = "/stats", method = RequestMethod.GET)
	public String stat(Model model) {
		model.addAttribute("cards", cards.findAll());

		if (!model.containsAttribute("card")) {
			Card card = new Card();
			model.addAttribute("card", card);
		}

		return "cardsstat";
	}
}
