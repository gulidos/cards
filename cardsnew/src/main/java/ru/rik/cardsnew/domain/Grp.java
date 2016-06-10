package ru.rik.cardsnew.domain;

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
import lombok.ToString;
import lombok.experimental.Builder;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString(exclude="cards")
//@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Grp {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter
    private long id;
	
	@Version
	@Getter @Setter
	protected long version;
	 
	@Getter @Setter
	@Column (nullable = false,  unique=true)
	String name;
	
	@Getter @Setter
    @OneToMany(mappedBy = "group")
    Set<Card> cards;
    
    @OneToMany(mappedBy = "group")
    Set<Channel> channels;

}
