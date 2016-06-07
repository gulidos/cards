package ru.rik.cardsnew.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
	
	@Getter @Setter
	@Column (nullable = false,  unique=true)
	String name;
	
	@Getter @Setter
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "group")
    Set<Card> cards = new HashSet<>();
    
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "group")
    Set<Channel> channels = new HashSet<>();

}
