package ru.rik.cardsnew.domain;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

@Entity @Table(name="_SMS")
@NoArgsConstructor  @AllArgsConstructor @Builder 
@EqualsAndHashCode(exclude = {"card", "channel"})
public class Sms {
	private static final Pattern balance = 
			Pattern.compile("^\\s*(Баланс.?|Минус.?|Balans.?|Balance.?|Minus.?):\\s*(-*\\d{1,4}[.,]\\d\\d)(\\s*р*.*)"
					, Pattern.MULTILINE);
	@Id  @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private long id;
	@Getter @Setter private int num;
	@Getter @Setter private String origAddress;
	@Getter @Setter private Date date;
	@Getter @Setter private String decodedmsg;
	@Transient
	@Getter @Setter private String encodedmsg;
	
	@ManyToOne
	@Getter @Setter private Card card;
	@ManyToOne
	@Getter @Setter private Channel channel;
	
	public Balance getBalance() {
		if (decodedmsg == null)
			return null;
		Matcher m = balance.matcher(decodedmsg);
		if (m.find()) 
			return Balance.builder().card(card).date(new Date()).decodedmsg(decodedmsg)
					.balance(Float.parseFloat(m.group(2).replace(',', '.')))
					.build();
		 else 	
			return null;
	}

	@Override
	public String toString() {
		return "Sms [id=" + id + ", num=" + num + ", origAddress=" + origAddress + ", date=" + date + ", decodedmsg="
				+ decodedmsg + ",  card=" + card.getName() + ", channel=" + channel.getName() + "]";
	}
	
}
