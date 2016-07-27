package ru.rik.cardsnew.web;

import java.util.List;

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

import ru.rik.cardsnew.db.BoxRepoImpl;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepoImpl;
import ru.rik.cardsnew.db.GroupRepoImpl;
import ru.rik.cardsnew.db.TrunkRepoImpl;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Line;
import ru.rik.cardsnew.domain.Oper;
import ru.rik.cardsnew.domain.Trunk;

@Controller
@RequestMapping("/chans")
@EnableTransactionManagement
public class ChannController {

	@Autowired GroupRepoImpl groups;
	@Autowired BoxRepoImpl boxes;
	@Autowired TrunkRepoImpl trunks;
	@Autowired ChannelRepoImpl chans;
	@Autowired CardRepo cards;
	
	public ChannController() {
		super();
	}
	
	@Transactional
	@RequestMapping(method = RequestMethod.GET)
	public String spittles(Model model) {
		List<Channel> list = chans.findAll();
		for (Channel ch: list) 
			ch.getTrunk().size();
		
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
		System.out.println("Reading a card for editing " + chan.toString());
		return "chan-edit";
	}

	
	@Transactional
	@RequestMapping(value="/edit", method=RequestMethod.GET)
	public String  editPage(@RequestParam(value="id", required=true) long id, Model model) {
		
		if(! model.containsAttribute("chan")) {
			Channel chan = chans.findById(id);
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
		model.addAttribute("trunks", trunks.findAll());
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
			model.addAttribute("message", message);
		} else if (result.hasErrors()) {
			System.out.println("there are validation errors" + result.getAllErrors().toString());
			redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.channel", result);
			redirectAttrs.addFlashAttribute("chan", chan);
			return "redirect:/chans/edit?id=" + chan.getId();
			
		} else if (action.equals("save") && chan != null) {
			Long boxId = chan.getBox() != null ? chan.getBox().getId() : null;
			chan.setBox(boxes.findById(boxId));
			Long groupId = chan.getGroup() != null ? chan.getGroup().getId() : null;
			Grp g = groups.findById(groupId); 
			chan.setGroup(g);
			Long trunkId = chan.getTrunk() != null ? chan.getTrunk().get(0).getId() : null; //FIXME !!! needs to do properly
			Trunk t = trunks.findById(trunkId);
			
			chan.getTrunk().add(t);
			
			chans.makePersistent(chan);
			String message = "Card " + chan.getId() + " was successfully edited";
			model.addAttribute("message", message);
		} 

		return "redirect:/chans";		
	}
}
