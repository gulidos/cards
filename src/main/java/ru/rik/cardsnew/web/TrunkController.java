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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.rik.cardsnew.db.BoxRepo;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.db.TrunkRepo;
import ru.rik.cardsnew.domain.Trunk;

@Controller
@RequestMapping("/trunks")
@SessionAttributes("filter") 
@EnableTransactionManagement
public class TrunkController {
	
	
	@Autowired BoxRepo boxes;
	@Autowired TrunkRepo trunks;
	@Autowired ChannelRepo chans;
	@Autowired CardRepo cards;
	@Autowired Filter filter;
	
	public TrunkController() { 
		super();
	}
	
	@Transactional
	@RequestMapping(method = RequestMethod.GET)
	public String trunks(Model model) {
		List<Trunk> list = trunks.findAll();
//		for (Trunk tr: list) { 
//			tr.getChannels().size();	
//		}	
		
		model.addAttribute("trunks", list);
		return "trunks";
	}

	@RequestMapping(value="/add", method=RequestMethod.GET) 
	public String  addEntity(Model model) {
		Trunk trunk = new Trunk();
		model.addAttribute("trunk", trunk);
		return "trunk-edit";
	}

	
	@Transactional
	@RequestMapping(value="/edit", method=RequestMethod.GET)
	public String  editPage(@RequestParam(value="id", required=true) long id, Model model) {
		filter.setId(id);
		
		if(!model.containsAttribute("trunk")) {
			Trunk trunk= trunks.findById(id);
			model.addAttribute("trunk", trunk);
			model.addAttribute("filter", filter);
		}
		return "trunk-edit";
	}
	
	
	@Transactional
	@RequestMapping(value="/edit", method=RequestMethod.POST)
	public String editEntity(
			@Valid @ModelAttribute Trunk trunk, 
			BindingResult result,
			Model model,  
			RedirectAttributes redirectAttrs,
			@RequestParam(value="action", required=true) String action ) {
		
		if (action.equals("cancel")) {
			String message = trunk.toString() + " edit cancelled";
			redirectAttrs.addFlashAttribute("message", message);
			
		} else if (result.hasErrors()) {
			redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.channel", result);
			redirectAttrs.addFlashAttribute("trunk", trunk);
			
			return "redirect:/trunks/edit?id=" + trunk.getId();
			
		} else if (action.equals("save") && trunk != null) {
			trunks.makePersistent(trunk);
		} 

		return "redirect:/trunks";		
	}
	
	@Transactional
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete (Model model,
			RedirectAttributes redirectAttrs,
			@RequestParam(value = "id", required = true) long id,
			@RequestParam(value="phase", required=true) String phase) {
		
		Trunk trunk = trunks.findById(id);
		String view = null;
		
		if (phase.equals("confirm")) {
			view ="redirect:/trunks";
			trunks.makeTransient(trunk);
			
			String message = "Trunk " + trunk.getName() + " was successfully deleted";
			redirectAttrs.addFlashAttribute("message", message);
		}
		
		return view;
	}
	

}
