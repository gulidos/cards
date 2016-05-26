package ru.rik.cardsnew.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

@Entity
public class Group {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
	
//    @Column(nullable = false, unique=true)
	String name;
    
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "group")
    Set<Card> cards = new HashSet<>();
    
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "group")
    Set<Channel> channels = new HashSet<>();

}
