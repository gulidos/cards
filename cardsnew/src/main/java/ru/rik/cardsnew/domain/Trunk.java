package ru.rik.cardsnew.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.springframework.cache.annotation.Cacheable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.rik.cardsnew.config.AppInitializer;
import ru.rik.cardsnew.domain.repo.TrunksStates;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = { "channels" })
@EqualsAndHashCode(exclude = { "channels" })
@Entity 
@Cacheable
@org.hibernate.annotations.Cache(
	    usage = CacheConcurrencyStrategy.READ_WRITE
//	    ,region = "trunkCache"
	)
@Table(name = "TRUNK")
public class Trunk {
//	@Transient
//	private static final TrunksStates trunksStats = (TrunksStates) AppInitializer.getContext().getBean("trunksStats");

	@Id	@Column(name = "id") @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Version @Getter @Setter
	protected long version;

	@Column(nullable = false, unique = true)
	@NaturalId(mutable = true)
	private String name;

	private String descr;
	
	@OneToMany(mappedBy = "trunk") 
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	List<Channel> channels = new ArrayList<>();

	public TrunkState getState() {
		TrunksStates trunksStats = (TrunksStates) AppInitializer.getContext().getBean("trunksStats");
		return trunksStats.findById(getId());
	}
	
	
	public int getFirst() {
		int first = 0;
		TrunkState ts = getState();
		if (ts == null) 
			throw new NullPointerException("TrunkState is null for Trunk " + getName());
		
		int n = getChannels().size();
		if (n == 0) return first;
		
		int next = ts.getNext();
		if (next < n) 
			return next;
		else 
			ts.setNext(0);
		return first;
	}
	
	
	public List<Channel> getChannelsSorted() {
		int start = getFirst();
		List<Channel> sorted = new ArrayList<>();
		for (int i = start; i <= getChannels().size()-1; i++) 
			sorted.add(channels.get(i));
		for (int i = 0; i < start; i++)
			sorted.add(channels.get(i));
		return sorted;
	}
}
