package ru.rik.cardsnew.web;

import java.util.List;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.rik.cardsnew.db.BoxRepoImpl;
import ru.rik.cardsnew.db.CardRepoImpl;
import ru.rik.cardsnew.db.ChannelRepoImpl;
import ru.rik.cardsnew.db.GroupRepoImpl;
import ru.rik.cardsnew.db.TrunkRepoImpl;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Line;
import ru.rik.cardsnew.domain.Oper;
import ru.rik.cardsnew.domain.Trunk;

@Controller
@RequestMapping("/channels")
@EnableTransactionManagement
public class ChannController {
	private static final Logger logger = LoggerFactory.getLogger(ChannController.class);		
	
	@Autowired GroupRepoImpl groups;
	@Autowired BoxRepoImpl boxes;
	@Autowired TrunkRepoImpl trunks;
	@Autowired ChannelRepoImpl chans;
	@Autowired CardRepoImpl cards;
	
	
	public ChannController() { 
		super();
	}
	
	@Transactional
	@RequestMapping(method = RequestMethod.GET)
	public String spittles(Model model) {
		List<Channel> list = chans.findAll();
		for (Channel ch: list) 
			ch.getTrunks().size();
		
		model.addAttribute("chans", list);
		
		if(! model.containsAttribute("chan")) {
			Card card = new Card();
			model.addAttribute("chan", card);
		}

		return "channels";
	}

	@RequestMapping(value="/add", method=RequestMethod.GET)
	public String  addEntity(Model model) {
		Channel chan = new 	Channel();
		addToModel(model, chan);
		return "chan-edit";
	}

	
	@Transactional
	@RequestMapping(value="/edit", method=RequestMethod.GET)
	public String  editPage(@RequestParam(value="id", required=true) long id, Model model) {
		
		if(! model.containsAttribute("chan")) {
			Channel chan = chans.findById(id);
			chan.getTrunks().size();
			addToModel(model, chan);
			
		}
		return "chan-edit";
	}
	
	private void addToModel(Model model, Channel chan) {
		model.addAttribute("chan", chan);
		model.addAttribute("opers", Oper.values()); 
		model.addAttribute("lines", Line.values());
		model.addAttribute("groups", groups.findAll());
		model.addAttribute("boxes", boxes.findAll());
		model.addAttribute("alltrunks", trunks.findAll());
		model.addAttribute("cards", cards.findGroupCards(chan.getGroup()));
	}
	
	@Transactional
	@RequestMapping(value="/edit", method=RequestMethod.POST)
	public String editEntity(
			@Valid @ModelAttribute Channel chan, 
			BindingResult result,
			Model model,  
			RedirectAttributes redirectAttrs,
			@RequestParam(value="action", required=true) String action ) {
		
		System.out.println("action: " + action + " card: " + chan.toString());
		if (action.equals("cancel")) {
			String message = chan.toString() + " edit cancelled";
			redirectAttrs.addFlashAttribute("message", message);
		} else if (result.hasErrors()) {
			System.out.println("there are validation errors" + result.getAllErrors().toString());
			redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.channel", result);
			redirectAttrs.addFlashAttribute("chan", chan);
			return "redirect:/channels/edit?id=" + chan.getId();
			
		} else if (action.equals("save") && chan != null) {
//			
//			List<Trunk> trunkId = chan.getTrunks();
//			Trunk t = trunks.findById(trunkId);
			
			
			Channel persChan = chans.makePersistent(chan);
			
			Card c = chan.getCard();
			if (c != null) {
//				c.setChannel(persChan);
				cards.makePersistent(c);
			}	 
			
			
			
			for (Trunk t: persChan.getTrunks())	
				t.getChannels().add(persChan);
		} 

		return "redirect:/channels";		
	}
	
	@Transactional
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String deleteChannel (Model model,
			RedirectAttributes redirectAttrs,
			@RequestParam(value = "id", required = true) long id,
			@RequestParam(value="phase", required=true) String phase) {
		
		Channel chan = chans.findById(id);
		String view = null;
		
		if (phase.equals("confirm")) {
			view ="redirect:/channels";
			chans.makeTransient(chan);
			
			String message = "Channel " + chan.getName() + " was successfully deleted";
			redirectAttrs.addFlashAttribute("message", message);
		}
		
		return view;
	}
	
	
	@RequestMapping(value = "/stats", method = RequestMethod.GET)
	public String stat(Model model) {
		List<Channel> list = chans.findAll();
		model.addAttribute("chans", list);
		return "channelsstat";
	}

}
