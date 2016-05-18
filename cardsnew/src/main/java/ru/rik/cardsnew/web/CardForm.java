package ru.rik.cardsnew.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import ru.rik.cardsnew.domain.Card;

public class CardForm {
	private long id;
	
	@NotNull(message = "{error.card.name.null}")
	@NotEmpty(message = "{error.card.name.empty}")
	@Size(min = 3, max = 20, message = "{error.card.name.size}")
	private String name;
	
	@NotNull(message = "{error.card.number.null}")
	@NotEmpty(message = "{error.card.number.empty}")
	@Size(min = 3, max = 20, message = "{error.card.number.size}")
	private String number;
	
	@NotNull(message = "{error.card.ip.null}")
	@NotEmpty(message = "{error.card.ip.empty}")
	@Size(min = 3, max = 20, message = "{error.card.ip.size}")
	private String ip;
	
	@NotNull(message = "{error.card.place.null}")
	@NotEmpty(message = "{error.card.place.empty}")
	@Size(min = 3, max = 20, message = "{error.card.place.size}")
	private String place;
	
	@NotNull(message = "{error.card.im.null}")
	@NotEmpty(message = "{error.card.im.empty}")
	@Size(min = 3, max = 20, message = "{error.card.im.size}")
	private String im;

	public CardForm() {
	};

	public CardForm(Card card) {
		super();
		this.id = card.getId();
		this.name = card.getName();
		this.number = card.getNumber();
		this.ip = card.getIp();
		this.place = card.getPlace();
		this.im = card.getImei();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getIm() {
		return im;
	}

	public void setIm(String im) {
		this.im = im;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
