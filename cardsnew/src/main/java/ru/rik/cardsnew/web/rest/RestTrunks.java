package ru.rik.cardsnew.web.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.GroupRepoImpl;
import ru.rik.cardsnew.db.TrunkRepoImpl;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Trunk;

@RestController
@RequestMapping("/rest")
@EnableTransactionManagement
public class RestTrunks {
	@Autowired
	GroupRepoImpl groups;
	@Autowired
	CardRepo cards;
	@Autowired
	TrunkRepoImpl trunks;

	public RestTrunks() {
	}

	@Transactional
	@RequestMapping(value = "/trunks", method = RequestMethod.GET)
	public List<RestTrunk> trunks() {
		List<RestTrunk> lst = new ArrayList<>();
		for (Trunk t : trunks.findAll()) {
			lst.add(new RestTrunk(t));
		}
		return lst;
	}

	@Transactional
	@RequestMapping(value = "/trunk/{id}", method = RequestMethod.GET)
	public RestTrunk get(@PathVariable("id") long id) {
		Trunk t = trunks.findById(id);
		if (t!= null) 
			return new RestTrunk(t);
		else return null;
	}

	@Data
	public class RestTrunk {
		long id;
		String name;
//		int next;
		int n;
		List<String> channels = new ArrayList<>();

		public RestTrunk(Trunk t) {
			id = t.getId();
			name = t.getName(); 
//			next = t.getNext();
			n = t.getChannels().size();
			for (Channel ch : t.getChannelsSorted()) 
				channels.add(ch.toString());
			
		}
	}

}
