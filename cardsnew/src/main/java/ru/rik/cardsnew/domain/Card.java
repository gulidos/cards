package ru.rik.cardsnew.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import ru.rik.cardsnew.web.CardForm;


@Entity
@Table(name="CARD")
public class Card {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

	private String name;
	private String ip;
	private String place;
	private String imei;


	private String number;
	
	public Card() {	
	}
	
	public Card(String name, String ip, String place, String imei, String number) {
		super();
		this.name = name;
		this.ip = ip;
		this.place = place;
		this.imei = imei;
		this.number = number;
	}
	
	public Card(CardForm cf) {
		super();
		setCard(cf);
	}
	
	public void setCard(CardForm cf) {
		this.name = cf.getName();
		this.ip = cf.getIp();
		this.place = cf.getPlace();
		this.imei = cf.getIm();
		this.number = cf.getNumber();
	}
	
	public long getId() {	return id;}
	public void setId(long id) {	this.id = id;	}

	public String getName() {	return name;	}
	public void setName(String name) {	this.name = name;	}
	
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
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "Card [id=" + id + ", name=" + name + ", ip=" + ip + ", place=" + place + ", imei=" + imei + ", number="
				+ number + "]";
	}
	
	
}
