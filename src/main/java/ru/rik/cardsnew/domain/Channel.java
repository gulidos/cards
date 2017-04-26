package ru.rik.cardsnew.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
import ru.rik.cardsnew.db.ChannelRepo;

@NamedQueries({  
	@NamedQuery(name = "findPair", query = "SELECT c FROM Channel c WHERE c.line = :line and c.box = :box"),
	@NamedQuery(name = "findByGrp", query = "SELECT c FROM Channel c WHERE c.group = :grp"),
	@NamedQuery(name = "findByBox", query = "SELECT c FROM Channel c WHERE c.box = :box")
	}
)
@NoArgsConstructor @AllArgsConstructor @Builder @ToString (exclude = {"box",  "group", "trunks", "card"})
@EqualsAndHashCode (exclude = {"box", "trunks", "group", "card"}, callSuper = false)
@Entity @Cacheable
@Table(name="_CHANNEL", uniqueConstraints=@UniqueConstraint(columnNames={"box_id", "line"}))
@org.hibernate.annotations.Cache(  usage = CacheConcurrencyStrategy.READ_WRITE	)
public class Channel implements MyEntity{
	
    @Id   @Column(name="id")   @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Getter @Setter
    private long id;
	
    @Version
 	@Getter @Setter
	protected long version;

	
	@Column(unique=true, nullable=false)
	@Size(min = 3, max = 20, message = "Bad value")
	@Setter @Getter private String name;
	
	@Setter @Getter private Line line;
	
	@ManyToOne(optional=false) 
	@Setter @Getter private Box box;
	
	 
	@ManyToOne(optional = false)
	@Setter	@Getter private Grp group;

	@Getter	@Setter
	@ManyToMany(cascade = CascadeType.PERSIST)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<Trunk> trunks = new HashSet<>();
	
	@Getter	@Setter
	@OneToOne  (fetch = FetchType.EAGER)  //the owner side. The inverse side is the one which has the mappedBy attribute (Card)
	@JoinColumn(name = "card", unique=true  )
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Card card;
	
	@Getter	@Setter	private boolean enabled;
	
	@Transient
	@Getter @Setter private ChannelState state;
	
	public ChannelState getState(ChannelRepo chans) {
		return chans.findStateById(getId());
	}
	
	public Channel getPair(ChannelRepo chans) {
		return chans.findPair(this);
	}
	
	
	
}
