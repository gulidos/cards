package ru.rik.cardsnew.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
import ru.rik.cardsnew.db.TrunkRepo;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = { "channels" })
@EqualsAndHashCode(exclude = { "channels" })
@Entity 
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "_TRUNK")
public class Trunk implements MyEntity {
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
	
	@ManyToMany(mappedBy = "trunks")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	Set<Channel> channels = new HashSet<>();

	public TrunkState getState() {
		TrunkRepo trunksStats = (TrunkRepo) AppInitializer.getContext().getBean("trunkRepoImpl");
		return trunksStats.findStateById(getId());
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
	
	public String toStringAll() {
		StringBuilder sb = new StringBuilder("");
		for (Channel ch: channels)
			sb.append(ch.getName());
		return toString() 
				+ " ch: "    + sb.toString() ;
	}
	
	
	public Set<Channel> getChannelsSorted() {
//		int start = getFirst();
//		List<Channel> sorted = new ArrayList<>();
//		for (int i = start; i <= getChannels().size()-1; i++) 
//			sorted.add(channels.get(i));
//		for (int i = 0; i < start; i++)
//			sorted.add(channels.get(i));
		return channels;
	}
}
