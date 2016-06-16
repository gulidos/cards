package ru.rik.cardsnew.domain;

public enum Place {
	b0000000(0),
	b0000001(1),
	b0000002(2),
	b0000003(3),
	b0000004(4),
	b0000005(5),
	b0000006(6),
	b0000007(7),
	b0000008(8),
	b0000009(9),
	b000000a(10);
	
	private final int id;
	
	Place(int id) {this.id = id;}
	
	public static Place getInstance(int id) {
		switch(id) {
			case 0: return Place.b0000000;
			case 1: return Place.b0000001;
			case 2: return Place.b0000002;
			case 3: return Place.b0000003;
			case 4: return Place.b0000004;
			case 5: return Place.b0000005;
			case 6: return Place.b0000006;
			case 7: return Place.b0000007;
			case 8: return Place.b0000008;
			case 9: return Place.b0000009;
			case 10: return Place.b000000a;
			default: return null;				
		}
	}

	public int getId() {	return id;	}
}
