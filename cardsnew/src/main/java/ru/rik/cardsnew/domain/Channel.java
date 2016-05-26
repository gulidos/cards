package ru.rik.cardsnew.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@EqualsAndHashCode (exclude = {"box", "trunk", "group"})
@ToString (exclude = {"box", "trunk", "group"})
@Entity
@Table(name="CHANNEL")
public class Channel {
    @Id   @Column(name="id")   @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Getter @Setter
    private long id;
	
	@Setter @Getter
	private String name;
	
	@Setter @Getter
	private Line line;
	
	@Setter @Getter
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Box box;
	
	@Setter	@Getter
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Group group;

	@Getter	@Setter
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Trunk trunk;
}
