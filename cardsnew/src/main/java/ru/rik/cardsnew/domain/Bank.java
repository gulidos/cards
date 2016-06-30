package ru.rik.cardsnew.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude={"cards"})
@EqualsAndHashCode(exclude={"cards"})
@Cacheable
@org.hibernate.annotations.Cache(
	    usage = CacheConcurrencyStrategy.READ_WRITE
	)
public class Bank {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter
    private long id;
	
	 @Version
	 @Getter @Setter
	 protected long version;
	 
    @Column (unique=true, nullable=false)
    @Getter @Setter
	String location;
    
    @Column (unique=true)
    @Getter @Setter
    private String ip;
    
    @Getter @Setter
//   @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "bank")
 @OneToMany(mappedBy = "bank")
    Set<Card> cards = new HashSet<>();
    
    public String getName() {
		return getIp();
	}
}
