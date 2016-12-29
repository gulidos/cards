package ru.rik.cardsnew.domain;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

@Entity @Table(name="_SMS")
@NoArgsConstructor  
@EqualsAndHashCode( callSuper = true)
public class Sms extends Event{
	private static final Pattern balance = 
			Pattern.compile("^\\s*(Баланс.?|Минус.?|Balans.?|Balance.?|Minus.?):\\s*(-*\\d{1,4}[.,]\\d\\d)(\\s*р*.*)"
					, Pattern.MULTILINE);
//	@Id  @GeneratedValue(strategy=GenerationType.IDENTITY)
//	@Getter @Setter private long id;
	@Getter @Setter private int num;
	@Getter @Setter private String origAddress;
//	@Getter @Setter private Date date;
	@Getter @Setter private String decodedmsg;
//	@Transient
//	@Getter @Setter private String encodedmsg;
	
//	@ManyToOne
//	@Getter @Setter private Card card;
//	@ManyToOne
//	@Getter @Setter private Channel channel;
	
	public Ussd getBalance() {
		if (decodedmsg == null)
			return null;
		Matcher m = balance.matcher(decodedmsg);
		if (m.find()) 
			return Ussd.builder().card(card).date(new Date()).decodedmsg(decodedmsg)
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

	@Builder
	public Sms(long id, Date date, Card card, Channel channel, int num, String origAddress, String decodedmsg) {
		super(id, date, card, channel);
		this.num = num;
		this.origAddress = origAddress;
		this.decodedmsg = decodedmsg;
	}
	
}
