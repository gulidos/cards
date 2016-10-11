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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.rik.cardsnew.db.BoxRepo;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.db.GroupRepo;
import ru.rik.cardsnew.db.TrunkRepo;
import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Grp;

@Controller
@RequestMapping("/box")
@SessionAttributes("filter") 
@EnableTransactionManagement
public class BoxController {
	private static final Logger logger = LoggerFactory.getLogger(BoxController.class);		
	
	@Autowired GroupRepo groups;
	@Autowired BoxRepo boxes;
	@Autowired TrunkRepo trunks;
	@Autowired ChannelRepo chans;
	@Autowired CardRepo cards;
	@Autowired Filter filter;
	
	public BoxController() { 
		super();
	}
	
	@Transactional
	@RequestMapping(method = RequestMethod.GET)
	public String boxes(Model model) {
		List<Box> list = boxes.findAll();	
		
		model.addAttribute("boxes", list);
		return "boxes";
	}

	@RequestMapping(value="/add", method=RequestMethod.GET) 
	public String  addEntity(Model model) {
		Box box = new Box();
		model.addAttribute("box", box);
		return "box-edit";
	}

	
	@Transactional
	@RequestMapping(value="/edit", method=RequestMethod.GET)
	public String  editPage(@RequestParam(value="id", required=true) long id, Model model) {
		filter.setId(id);
		
		if(!model.containsAttribute("box")) {
			Box box= boxes.findById(id);
			model.addAttribute("box", box);
			model.addAttribute("filter", filter);
		}
		return "box-edit";
	}
	
	
	@Transactional
	@RequestMapping(value="/edit", method=RequestMethod.POST)
	public String editEntity(
			@Valid @ModelAttribute Box box, 
			BindingResult result,
			Model model,  
			RedirectAttributes redirectAttrs,
			@RequestParam(value="action", required=true) String action ) {
		
		if (action.equals("cancel")) {
			String message = box.toString() + " edit cancelled";
			redirectAttrs.addFlashAttribute("message", message);
			
		} else if (result.hasErrors()) {
			System.out.println("there are validation errors" + result.getAllErrors().toString());
			redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.channel", result);
			redirectAttrs.addFlashAttribute("box", box);
			
			return "redirect:/box/edit?id=" + box.getId();
			
		} else if (action.equals("save") && box != null) {
			boxes.makePersistent(box);
		} 

		return "redirect:/box";		
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
