package ru.rik.cardsnew.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity


public class Oper {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
	
//	@Column(nullable = false, unique=true)
	String name;
	
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "oper")
    Set<Card> cards = new HashSet<>();
	
}
