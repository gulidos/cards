package ru.rik.cardsnew.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@EqualsAndHashCode(exclude = {"card", "channel"})
@NoArgsConstructor  
public abstract class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)

	@Getter @Setter protected long id;
	@Getter @Setter protected Date date;
	@ManyToOne
	@Getter @Setter protected Card card;
	@ManyToOne
	@Getter @Setter protected Channel channel;
	
	
	public Event(long id, Date date, Card card, Channel channel) {
		super();
		this.id = id;
		this.date = date;
		this.card = card;
		this.channel = channel;
	}
	
	

}
