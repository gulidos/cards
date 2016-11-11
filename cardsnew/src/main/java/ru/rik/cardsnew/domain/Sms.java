package ru.rik.cardsnew.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

@Entity
@NoArgsConstructor  @AllArgsConstructor @Builder
public class Sms {
	@Id
	@Getter @Setter private long id;
	@Getter @Setter private int num;
	@Getter @Setter private String origAddress;
	@Getter @Setter private Date date;
	@Getter @Setter private String decodedmsg;
	@Transient
	@Getter @Setter private String encodedmsg;
	@Getter @Setter private Card card;
	@Getter @Setter private Channel channel;
	
}
