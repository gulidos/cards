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

import lombok.Data;

@Data
@Entity
public class Bank {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
	
    @Column (unique=true)
	String location;
    
    @Column (nullable = false,  unique=true)
    String ip;
    
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "bank")
    Set<Card> cards = new HashSet<>();

}
