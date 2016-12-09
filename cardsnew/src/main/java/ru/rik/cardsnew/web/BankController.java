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

import ru.rik.cardsnew.db.BankRepo;
import ru.rik.cardsnew.domain.Bank;

@Controller
@RequestMapping("/banks")
@SessionAttributes("filter") 
@EnableTransactionManagement
public class BankController {	
	
	@Autowired BankRepo banks;
	@Autowired Filter filter;
	
	public BankController() { 
		super();
	}
	
	@Transactional
	@RequestMapping(method = RequestMethod.GET)
	public String banks(Model model) {
		List<Bank> list = banks.findAll();	
		list.forEach(b -> b.setState(banks.findStateById(b.getId())));
		model.addAttribute("banks", list);
		return "banks";
	}

	@RequestMapping(value="/add", method=RequestMethod.GET) 
	public String  addEntity(Model model) {
		Bank bank = new Bank();
		model.addAttribute("bank", bank);
		return "bank-edit";
	}

	
	@Transactional
	@RequestMapping(value="/edit", method=RequestMethod.GET)
	public String  editPage(@RequestParam(value="id", required=true) long id, Model model) {
		filter.setId(id);
		
		if(!model.containsAttribute("bank")) {
			Bank bank= banks.findById(id);
			model.addAttribute("bank", bank);
			model.addAttribute("filter", filter);
		}
		return "bank-edit";
	}
	
	
	@Transactional
	@RequestMapping(value="/edit", method=RequestMethod.POST)
	public String editEntity(
			@Valid @ModelAttribute Bank bank, 
			BindingResult result,
			Model model,  
			RedirectAttributes redirectAttrs,
			@RequestParam(value="action", required=true) String action ) {
		
		if (action.equals("cancel")) {
			String message = bank.toString() + " edit cancelled";
			redirectAttrs.addFlashAttribute("message", message);
			
		} else if (result.hasErrors()) {
			System.out.println("there are validation errors" + result.getAllErrors().toString());
			redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.channel", result);
			redirectAttrs.addFlashAttribute("bank", bank);
			
			return "redirect:/bank/edit?id=" + bank.getId();
			
		} else if (action.equals("save") && bank != null) {
			banks.makePersistent(bank);
		} 

		return "redirect:/banks";		
	}
	
	@Transactional
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete (Model model,
			RedirectAttributes redirectAttrs,
			@RequestParam(value = "id", required = true) long id,
			@RequestParam(value="phase", required=true) String phase) {
		
		Bank bank = banks.findById(id);
		String view = null;
		
		if (phase.equals("confirm")) {
			view ="redirect:/banks";
			banks.makeTransient(bank);
			String message = "bank " + bank.getName() + " was successfully deleted";
			redirectAttrs.addFlashAttribute("message", message);
		}
		
		return view;
	}
	

}
