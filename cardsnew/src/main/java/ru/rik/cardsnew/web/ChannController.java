package ru.rik.cardsnew.web;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.rik.cardsnew.db.BankRepo;
import ru.rik.cardsnew.db.BoxRepo;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.db.GroupRepo;
import ru.rik.cardsnew.db.TrunkRepo;
import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.ChannelState.Status;
import ru.rik.cardsnew.domain.Grp;
import ru.rik.cardsnew.domain.Line;
import ru.rik.cardsnew.domain.Oper;
import ru.rik.cardsnew.domain.State;
import ru.rik.cardsnew.domain.Trunk;
import ru.rik.cardsnew.service.Switcher;
import ru.rik.cardsnew.service.TaskCompleter;
import ru.rik.cardsnew.service.asterisk.AsteriskEvents;
import ru.rik.cardsnew.service.http.GsmState;
import ru.rik.cardsnew.service.http.SimSet;

@Controller
@RequestMapping("/channels")
@SessionAttributes("filter") 
@EnableTransactionManagement
public class ChannController {
	private static final Logger logger = LoggerFactory.getLogger(ChannController.class);		
	
	@Autowired GroupRepo groups;
	@Autowired BoxRepo boxes;
	@Autowired BankRepo banks;
	@Autowired TrunkRepo trunks;
	@Autowired ChannelRepo chans;
	@Autowired CardRepo cards;
	@Autowired Filter filter;
	@Autowired Switcher switcher;
	@Autowired TaskCompleter taskCompleter;
	@Autowired private AsteriskEvents astMngr;
	
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
		if (list != null)
			for (Channel ch: list ) 
				ch.setState(chans.findStateById(ch.getId()));

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
			Card c = persChan.getCard();
			logger.debug("card in channel", c!= null ? c.getName() : "none");
			if (c != null) {
				c.setChannelId(chan.getId());
				cards.makePersistent(c);
			}	
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
		Channel chan = chans.findById(id);
		ChannelState st = chans.findStateById(id);
		model.addAttribute("state", st);
		model.addAttribute("chan", chan);
		model.addAttribute("stateText", st.toWeb(cards, chans, banks, astMngr));
		
		Channel peer = chan.getPair(chans);
		model.addAttribute("peerText", peer.getState(chans).toWeb(cards, chans, banks, astMngr));
		
		List<Card> listcards = cards.findFreeCardsInGroup(chan.getGroup());
		if (chan.getCard() != null) {
			Card c = cards.findById(chan.getCard().getId());
			listcards.add(c);
		}
		model.addAttribute("cards", listcards);
		return "chan-stat";
	}
	
	@Transactional
	@RequestMapping(value="/chanstats/statistic", method=RequestMethod.POST)
	public String refreshChanStat(
			@Valid @ModelAttribute ChannelState state, 
			BindingResult result,
			Model model,  
			RedirectAttributes redirectAttrs,
			@RequestParam(value="action", required=true) String action ) {
			logger.debug("State: {}", state.toString());
				if (action.equals("cancel")) {
//					String message = chan.toString() + " edit cancelled";
//					redirectAttrs.addFlashAttribute("message", message);
					
				} else if (action.equals("gsmreq") && state != null) {
					Channel ch = chans.findById(state.getId());
					ChannelState st = ch.getState(chans);
					try {
						GsmState gstate = GsmState.get(ch);
						st.applyGsmStatus(gstate);
						SimSet simset = SimSet.get(ch, ch.getPair(chans));
						st.applySimSet(simset);
					} catch (SocketTimeoutException | ConnectException e) {
						st.setStatus(Status.Unreach);
						logger.debug("channel {} is unreachable", st.getName());
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}	
					
					return "redirect:/channels/stat" + "?id=" + state.getId();
				} 
				
				if (!filter.getUrl().isEmpty())
					return "redirect:/channels/chanstats/?url=" + filter.getUrl() + "&id=" + filter.getId();

				return "redirect:/channels/chanstats";		
	}
			
	
	@RequestMapping(value = "/chanstats/switch", method = RequestMethod.POST)
	public String switchCardWeb(
			@ModelAttribute Channel chan,
			Model model, RedirectAttributes redirectAttrs,
			@RequestParam(value = "action", required = true) String action) {
		Assert.notNull(chan);
		logger.debug("Channel: {} ", chan.toString() );
		Channel persCh = chans.findById(chan.getId());
		ChannelState st = chans.findStateById(persCh.getId());

		if (action.equals("install")) {
			Card newCard = chan.getCard(); 
			Card persCard = cards.findById(newCard.getId());
			Callable<State> switchCard = () ->  switcher.switchCard(persCh, persCard);
			taskCompleter.addTask(switchCard, st);
			
		} else if (action.equals("clear")) {
			Callable<State> switchCard = () ->  switcher.switchCard(persCh, null);
			taskCompleter.addTask(switchCard, st);
		}
		if (!filter.getUrl().isEmpty()) 
			return "redirect:/channels/chanstats/?url=" + filter.getUrl() + "&id=" + filter.getId();

		 return "redirect:/channels/stat" + "?id=" + chan.getId();

	}
	
	

}
