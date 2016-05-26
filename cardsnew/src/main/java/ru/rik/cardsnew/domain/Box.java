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

@Data
@Entity
@Table(name="BOX")
public class Box {
    @Id   @Column(name="id")   @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
	private String disposition;
	private String ip;
	private int capacity;
	private String descr;
	
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "box")
    Set<Channel> channels = new HashSet<>();


}
