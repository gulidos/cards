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
import ru.rik.cardsnew.web.CardForm;

@NoArgsConstructor
@EqualsAndHashCode(exclude = {"oper", "bank", "group"})
@ToString(exclude = {"oper", "bank", "group"})
@Entity
@Table(name="CARD")
public class Card {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Getter @Setter
    private long id;
    
    @Getter @Setter
    @Column(unique=true)
	private String name;
    
    @Getter @Setter
	private Place place;
    
    @Getter @Setter
	private String sernumber;
    
    @Getter @Setter
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Oper oper;
    
    @Getter @Setter
    @ManyToOne(cascade = CascadeType.PERSIST)
	private Group group;
    
    @Getter @Setter
    @ManyToOne(cascade = CascadeType.PERSIST)
	private Bank bank;
    
    @Getter @Setter
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
