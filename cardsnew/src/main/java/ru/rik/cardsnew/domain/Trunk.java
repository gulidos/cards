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
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Entity
@Table(name="TRUNK")
public class Trunk {
    @Id   @Column(name="id")   @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
	
	private String name;
	private String descr;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "trunk")
    Set<Channel> channel = new HashSet<>();

}
