package ru.rik.cardsnew.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rik.cardsnew.web.CardForm;

@Data
@NoArgsConstructor
@Entity
@Table(name="CARD")
public class Card {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    @Column(unique=true)
	private String name;
	private Place place;
	private String sernumber;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Oper oper;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Trunk trunk;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Bank bank;

	private String number;
	

	
	public Card(CardForm cf) {
		super();
		setCard(cf);
	}
	
	public void setCard(CardForm cf) {
		this.name = cf.getName();
//		this.place = cf.getPlace();
		this.sernumber = cf.getIm();
		this.number = cf.getNumber();
	}
	
}
