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
	@NamedQuery(name = "findAllBalance", query = "SELECT u FROM Ussd u "),
	@NamedQuery(name = "findAllLastBalance", query = "SELECT b FROM Ussd b WHERE b.date = "
			+ "(SELECT MAX(b1.date) FROM Ussd b1 WHERE b1.card = b.card) and b.balance <> NULL"),
	@NamedQuery(name = "findCardBalance", query = "SELECT u FROM Ussd u ")
	}
)
public class Ussd extends Event{
	@Getter @Setter private String decodedmsg;
	@Getter @Setter private Float balance;
	@Getter @Setter private boolean payment; //set up manually via web ui
	@Getter @Setter private boolean smsNeeded;
	
	@Override
	public String toString() {
		return "Balance [id=" + id + ", date=" + date + ", decodedmsg=" + decodedmsg + ", balance=" + balance
				+ ", card=" + card.getId() + ", payment=" + payment + ", smsNeeded=" + smsNeeded + "]";
	}

	@Builder
	public Ussd(long id, Date date, Card card, Channel channel, String decodedmsg, Float balance, boolean payment,
			boolean smsNeeded) {
		super(id, date, card, channel);
		this.decodedmsg = decodedmsg;
		this.balance = balance;
		this.payment = payment;
		this.smsNeeded = smsNeeded;
	}
	
	
}
