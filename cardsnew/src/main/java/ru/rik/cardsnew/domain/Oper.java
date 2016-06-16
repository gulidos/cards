package ru.rik.cardsnew.domain;

public enum Oper {
	RED(0), GREEN(1), YELLOW(2), UNKNOWN(3);
	private final int id;

	Oper(int v) {this.id = v;}
	
	public static Oper getInstance(int id) {
		switch(id) {
			case 0: return Oper.RED;
			case 1: return Oper.GREEN;
			case 2: return Oper.YELLOW;
			default: return Oper.UNKNOWN;				
		}
	}
	
	public int getId () {return this.id;}
}
