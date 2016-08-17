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
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepoImpl;
import ru.rik.cardsnew.db.GroupRepoImpl;
import ru.rik.cardsnew.db.TrunkRepoImpl;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Oper;
import ru.rik.cardsnew.domain.Trunk;

@Controller
@RequestMapping("/trunks")
@EnableTransactionManagement
public class TrunkController {
	private static final Logger logger = LoggerFactory.getLogger(TrunkController.class);		
	
	@Autowired GroupRepoImpl groups;
	@Autowired BoxRepoImpl boxes;
	@Autowired TrunkRepoImpl trunks;
	@Autowired ChannelRepoImpl chans;
	@Autowired CardRepo cards;
	
	public TrunkController() { 
		super();
	}
	
	@Transactional
	@RequestMapping(method = RequestMethod.GET)
	public String spittles(Model model) {
		List<Trunk> list = trunks.findAll();
		for (Trunk tr: list) { 
			tr.getChannels().size();	
		}	
		
		model.addAttribute("trunks", list);
		return "trunks";
	}

	@RequestMapping(value="/add", method=RequestMethod.GET) 
	public String  addEntity(Model model) {
		Grp group = new Grp();
		model.addAttribute("group", group);
		return "group-edit";
	}

	
	@Transactional
	@RequestMapping(value="/edit", method=RequestMethod.GET)
	public String  editPage(@RequestParam(value="id", required=true) long id, Model model) {
		
		if(! model.containsAttribute("group")) {
			Grp group = groups.findById(id);
			group.getCards().size();
			group.getChannels().size();
			addToModel(model, group);	
		}
		return "group-edit";
	}
	
	private void addToModel(Model model, Grp group) {
		model.addAttribute("group", group);
		model.addAttribute("opers", Oper.values()); 
	}
	
	@Transactional
	@RequestMapping(value="/edit", method=RequestMethod.POST)
	public String editEntity(
			@Valid @ModelAttribute Grp group, 
			BindingResult result,
			Model model,  
			RedirectAttributes redirectAttrs,
			@RequestParam(value="action", required=true) String action ) {
		
		if (action.equals("cancel")) {
			String message = group.toString() + " edit cancelled";
			redirectAttrs.addFlashAttribute("message", message);
			
		} else if (result.hasErrors()) {
			System.out.println("there are validation errors" + result.getAllErrors().toString());
			redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.channel", result);
			redirectAttrs.addFlashAttribute("group", group);
			
			return "redirect:/groups/edit?id=" + group.getId();
			
		} else if (action.equals("save") && group != null) {
			groups.makePersistent(group);
		} 

		return "redirect:/groups";		
	}
	
	@Transactional
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete (Model model,
			RedirectAttributes redirectAttrs,
			@RequestParam(value = "id", required = true) long id,
			@RequestParam(value="phase", required=true) String phase) {
		
		Grp group = groups.findById(id);
		String view = null;
		
		if (phase.equals("confirm")) {
			view ="redirect:/groups";
			groups.makeTransient(group);
			
			String message = "Channel " + group.getName() + " was successfully deleted";
			redirectAttrs.addFlashAttribute("message", message);
		}
		
		return view;
	}
	

}