package ru.rik.cardsnew.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

@Entity @Table(name="_BALANCE")
@NoArgsConstructor  
@EqualsAndHashCode( callSuper = true)
@NamedQueries({ 
	@NamedQuery(name = "findAllBalance", query = "SELECT u FROM Balance u "),
	@NamedQuery(name = "findAllLastBalance", query = "SELECT b FROM Balance b WHERE b.date = "
			+ "(SELECT MAX(b1.date) FROM Balance b1 WHERE b1.card = b.card)"),
	@NamedQuery(name = "findCardBalance", query = "SELECT u FROM Balance u ")
	}
)
public class Balance extends Event{
//	@Id  @GeneratedValue(strategy=GenerationType.IDENTITY)
//	@Getter @Setter private long id;
//	@Getter @Setter private Date date;
	@Getter @Setter private String decodedmsg;
	@Getter @Setter private Float balance;
//	@ManyToOne(optional = false)
//	@Getter @Setter private Card card;
	@Getter @Setter private boolean payment;
	@Getter @Setter private boolean smsNeeded;
	
	@Override
	public String toString() {
		return "Balance [id=" + id + ", date=" + date + ", decodedmsg=" + decodedmsg + ", balance=" + balance
				+ ", card=" + card.getId() + ", payment=" + payment + ", smsNeeded=" + smsNeeded + "]";
	}

	@Builder
	public Balance(long id, Date date, Card card, Channel channel, String decodedmsg, Float balance, boolean payment,
			boolean smsNeeded) {
		super(id, date, card, channel);
		this.decodedmsg = decodedmsg;
		this.balance = balance;
		this.payment = payment;
		this.smsNeeded = smsNeeded;
	}
	
	
}
