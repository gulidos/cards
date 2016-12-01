package ru.rik.cardsnew.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import ru.rik.cardsnew.db.BoxRepo;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.db.TrunkRepo;
import ru.rik.cardsnew.domain.State;
import ru.rik.cardsnew.service.TaskCompleter;
import ru.rik.cardsnew.service.TaskDescr;

@Controller
@RequestMapping("/activity")
@SessionAttributes("filter") 
@EnableTransactionManagement
public class ActivityController {
	
	@Autowired BoxRepo boxes;
	@Autowired TrunkRepo trunks;
	@Autowired ChannelRepo chans;
	@Autowired CardRepo cards;
	@Autowired Filter filter;
	@Autowired TaskCompleter taskCompleter;
	
	public ActivityController() { 
		super();
	}


	@RequestMapping(value="/background", method=RequestMethod.GET) 
	public String  addEntity(Model model) {
		Map<Future<State>, TaskDescr> map = taskCompleter.getMap();
		List<String> list = new ArrayList<>();
		for (Future<State> s: map.keySet()) {
			TaskDescr descr = map.get(s);
			String str = descr.getName() + " " + descr.getClazz().getSimpleName() + " " 
			+ (new Date().getTime() - descr.getLastChangeDate().getTime())/1000 +
					" " + descr.getStage() ;
			list.add(str);

		}	
		model.addAttribute("tasks", list);
		return "tasks";
	}

	
	

}
