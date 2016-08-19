package ru.rik.cardsnew.domain;

public class GrpState implements State {
	private long id;
	private String name;
	
	public GrpState() {
	}

	@Override public long getId() {	return id;}
	@Override public void setId(long id) { this.id = id;}
	
	@Override public String getName() {return name;}
	@Override public void setName(String name) {this.name = name;}
}
