package ru.rik.cardsnew.domain;

import java.util.Date;

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
import lombok.ToString;
import lombok.experimental.Builder;

@Entity @Table(name="_SMS")
@NoArgsConstructor  @AllArgsConstructor @Builder @ToString(exclude = {"card", "channel"}) 
@EqualsAndHashCode(exclude = {"card", "channel"})
public class Sms {
	@Id  @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private long id;
	@Getter @Setter private int num;
	@Getter @Setter private String origAddress;
	@Getter @Setter private Date date;
	@Transient
	@Getter @Setter private String decodedmsg;
	@Getter @Setter private String encodedmsg;
	
	@ManyToOne
	@Getter @Setter private Card card;
	@ManyToOne
	@Getter @Setter private Channel channel;
	
}
