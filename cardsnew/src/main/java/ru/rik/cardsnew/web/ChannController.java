package ru.rik.cardsnew.web;

import java.io.IOException;
import java.util.ArrayList;
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
import ru.rik.cardsnew.db.CardRepoImpl;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.db.GroupRepo;
import ru.rik.cardsnew.db.TrunkRepoImpl;
import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Line;
import ru.rik.cardsnew.domain.Oper;
import ru.rik.cardsnew.domain.Trunk;
import ru.rik.cardsnew.service.http.GsmState;

@Controller
@RequestMapping("/channels")
@SessionAttributes("filter") 
@EnableTransactionManagement
public class ChannController {
	private static final Logger logger = LoggerFactory.getLogger(ChannController.class);		
	
	@Autowired GroupRepo groups;
	@Autowired BoxRepo boxes;
	@Autowired TrunkRepoImpl trunks;
	@Autowired ChannelRepo chans;
	@Autowired CardRepoImpl cards;
	@Autowired Filter filter;

	
	public ChannController() { 	super();}
	
	
	@RequestMapping(method = RequestMethod.GET) @Transactional
	public String channels(
			@RequestParam(value = "id", defaultValue = "0") long id, 
			@RequestParam(value = "url", defaultValue = "") String url,
			Model model) {
		List<Channel> list = getChannels(id, url);
		filter.setId(id);
		model.addAttribute("chans", list);
		model.addAttribute("filter", filter);
		
		if(! model.containsAttribute("chan")) {
			Card card = new Card();
			model.addAttribute("chan", card);
		}
		return "channels";
	}


	private List<Channel> getChannels(long id, String url) {
		List<Channel> list = null;
		if (url.isEmpty()) {
			filter.setUrl("");
			list = chans.findAll();
		} else if ("trunk".equals(url)) {
			filter.setUrl("trunk");
			Trunk t = trunks.findById(id);
			if (t != null)
				list = new ArrayList<>(t.getChannels());
		} else if ("group".equals(url)) {
			filter.setUrl("group");
			Grp grp = groups.findById(id);
			if (grp != null)
				list = chans.findGroupChans(grp);
		} else if ("box".equals(url)) {
			filter.setUrl("box");
			Box box = boxes.findById(id);
			if (box != null)
				list = chans.findBoxChans(box);
		}
		return list;
	}
	
	
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public String  addEntity(Model model) {
		addToModel(model, new Channel());
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
		
		if (action.equals("cancel")) {
//			String message = chan.toString() + " edit cancelled";
//			redirectAttrs.addFlashAttribute("message", message);
		} else if (result.hasErrors()) {
			redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.channel", result);
			redirectAttrs.addFlashAttribute("chan", chan);
			return "redirect:/channels/edit?id=" + chan.getId();
		} else if (action.equals("save") && chan != null) {
			Channel persChan = chans.makePersistent(chan);
			Card c = chan.getCard();
			if (c != null) 
				cards.makePersistent(c);
			
			for (Trunk t: persChan.getTrunks())	
				t.getChannels().add(persChan);
		} 
		if (!filter.getUrl().isEmpty())
			return "redirect:/channels/?url=" + filter.getUrl() + "&id=" + filter.getId();

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
	

	//============ State's methods ================

	@Transactional
	@RequestMapping(value = "/chanstats", method = RequestMethod.GET) // list of states on one page
	public String chanStats(@RequestParam(value = "id", defaultValue = "0") long id, 
			@RequestParam(value = "url", defaultValue = "") String url,
			Model model) {
		List<Channel> list = getChannels(id, url);
		filter.setId(id);
		model.addAttribute("chans", list);
		model.addAttribute("filter", filter);
		return "channelstats";
	}
	
	
	@Transactional
	@RequestMapping(value = "/stat", method = RequestMethod.GET)
	public String statPage(@RequestParam(value = "id", required = true) long id, Model model) {
		ChannelState state = chans.findStateById(id);
		model.addAttribute("state", state);
		return "chan-stat";
	}
	
	@Transactional
	@RequestMapping(value="/chanstats/edit", method=RequestMethod.POST)
	public String editChanStat(
			@Valid @ModelAttribute ChannelState state, 
			BindingResult result,
			Model model,  
			RedirectAttributes redirectAttrs,
			@RequestParam(value="action", required=true) String action ) {
				if (action.equals("cancel")) {
//					String message = chan.toString() + " edit cancelled";
//					redirectAttrs.addFlashAttribute("message", message);
					
				} else if (action.equals("gsmreq") && state != null) {
					try {
						Channel ch = chans.findById(state.getId());
						GsmState gstate = GsmState.get(ch);
						ChannelState st = ch.getState();
						st.applyGsmStatu(gstate);
					} catch (IllegalAccessException | IOException e) {
						logger.error(e.getMessage(), e);
					}
					return "redirect:/channels/stat" + "?id=" + state.getId();
				} 
				if (!filter.getUrl().isEmpty())
					return "redirect:/channels/chanstats/?url=" + filter.getUrl() + "&id=" + filter.getId();

				return "redirect:/channels/chanstats";		
	}
			

}
