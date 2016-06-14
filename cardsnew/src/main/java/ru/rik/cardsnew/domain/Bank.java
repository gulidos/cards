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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bank extends MyEntity {
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

	@Override
	public String getName() {
		return getIp();
	}

}
