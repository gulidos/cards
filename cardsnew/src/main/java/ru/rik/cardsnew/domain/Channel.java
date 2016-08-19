package ru.rik.cardsnew.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Builder;
import ru.rik.cardsnew.config.AppInitializer;
import ru.rik.cardsnew.db.ChannelRepoImpl;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode (exclude = {"box", "trunks", "group", "card"}, callSuper = false)
@ToString (exclude = {"box",  "group"})
@Entity
@Table(name="_CHANNEL", uniqueConstraints=@UniqueConstraint(columnNames={"box_id", "line"}))
@Cacheable
@org.hibernate.annotations.Cache(  usage = CacheConcurrencyStrategy.READ_WRITE	)
public class Channel implements State{
	@Transient private static ChannelRepoImpl channelRepoImpl;
	@Transient private static Object sync = new Object();
	
    @Id   @Column(name="id")   @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Getter @Setter
    private long id;
	
    @Version
 	@Getter @Setter
	protected long version;

	@Setter @Getter
	@Column(unique=true, nullable=false)
	@Size(min = 3, max = 20, message = "Bad value")
	private String name;
	
	@Setter @Getter
	private Line line;
	
	@Setter @Getter
	@ManyToOne(optional=false) 
	private Box box;
	
	@Setter	@Getter  
	@ManyToOne 
	private Grp group;

	@Getter	@Setter
	@ManyToMany(cascade = CascadeType.PERSIST)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<Trunk> trunks = new HashSet<>();
	
	@Getter	@Setter
	@OneToOne    //the owner side. The inverse side is the one which has the mappedBy attribute (Card)
	@JoinColumn(name = "card", unique=true  )
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Card card;
	
	@Getter	@Setter
	private boolean enabled;

	public ChannelState getState() {

		if (channelRepoImpl == null) {
			synchronized (sync) {
				channelRepoImpl = (ChannelRepoImpl) AppInitializer.getContext().getBean("channelRepoImpl");
			}
		}	
		return channelRepoImpl.findStateById(getId());
	}
	
	
	
}
