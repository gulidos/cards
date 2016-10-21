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
	b000000a(10),
	b000000b(11),
	b000000c(12),
	b000000d(13),
	b000000e(14),
	b000000f(15),
	b0000010(16),
	b0000011(17),
	b0000012(18),
	b0000013(19),
	b0000014(20),
	b0000015(21),
	b0000016(22),
	b0000017(23),
	b0000018(24),
	b0000019(25),
	b000001a(26),
	b000001b(27),
	b000001c(28),
	b000001d(29),
	b000001e(30),
	b000001f(31),
	b0000020(32);
	
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
			case 11: return Place.b000000b;
			case 12: return Place.b000000c;
			case 13: return Place.b000000d;
			case 14: return Place.b000000e;
			case 15: return Place.b000000f;
			case 16: return Place.b0000010;
			case 17: return Place.b0000011;
			case 18: return Place.b0000012;
			case 19: return Place.b0000013;
			case 20: return Place.b0000014;
			case 21: return Place.b0000015;
			case 22: return Place.b0000016;
			case 23: return Place.b0000017;
			case 24: return Place.b0000018;
			case 25: return Place.b0000019;
			case 26: return Place.b000001a;
			case 27: return Place.b000001b;
			case 28: return Place.b000001c;
			case 29: return Place.b000001d;
			case 30: return Place.b000001e;
			case 31: return Place.b000001f;
			case 32: return Place.b0000020;
	
			default: return null;				
		}
	}

	public int getId() {	return id;	}
}
