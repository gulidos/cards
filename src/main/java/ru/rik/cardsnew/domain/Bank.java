package ru.rik.cardsnew.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Builder;
import ru.rik.cardsnew.db.BankRepo;
import ru.rik.cardsnew.db.ChannelRepo;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
@Entity
@ToString(exclude={"cards"})
@EqualsAndHashCode(exclude={"cards"})
@Cacheable
@org.hibernate.annotations.Cache( usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name="_BANK")
public class Bank implements MyEntity {
	@Transient public static final String DEF_USER = "voip";
	@Transient public static final String DEF_PASSWORD = "1234";
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter
    private long id;
	
	 @Version
	 @Getter @Setter
	 protected long version;
	 
    @Column (unique=true, nullable=false)
    @Getter @Setter	String location;
    
    @Column (unique=true)
    @Getter @Setter private String name;
    
    @Getter @Setter
    @OneToMany(mappedBy = "bank", fetch=FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Card> cards = new HashSet<>();

    @Transient
	@Getter @Setter private BankState state;

    public BankState getStat (BankRepo repo) {
    	return repo.findStateById(id);
    }

	
	public ChannelState getState(ChannelRepo chans) {
		return chans.findStateById(getId());
	}
//    @Override public String getName() {return getIp();}
//	@Override public void setName(String name) {this.ip = name;}
}
