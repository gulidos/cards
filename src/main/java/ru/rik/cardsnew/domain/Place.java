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
	 
	public static Place getInstance(String s) {
		switch(s) {
		case "b0000000": return Place.b0000000;
		case "b0000001": return Place.b0000001;
		case "b0000002": return Place.b0000002;
		case "b0000003": return Place.b0000003;
		case "b0000004": return Place.b0000004;
		case "b0000005": return Place.b0000005;
		case "b0000006": return Place.b0000006;
		case "b0000007": return Place.b0000007;
		case "b0000008": return Place.b0000008;
		case "b0000009": return Place.b0000009;
		case "b000000a": return Place.b000000a;
		case "b000000b": return Place.b000000b;
		case "b000000c": return Place.b000000c;
		case "b000000d": return Place.b000000d;
		case "b000000e": return Place.b000000e;
		case "b000000f": return Place.b000000f;
		case "b0000010": return Place.b0000010;
		case "b0000011": return Place.b0000011;
		case "b0000012": return Place.b0000012;
		case "b0000013": return Place.b0000013;
		case "b0000014": return Place.b0000014;
		case "b0000015": return Place.b0000015;
		case "b0000016": return Place.b0000016;
		case "b0000017": return Place.b0000017;
		case "b0000018": return Place.b0000018;
		case "b0000019": return Place.b0000019;
		case "b000001a": return Place.b000001a;
		case "b000001b": return Place.b000001b;
		case "b000001c": return Place.b000001c;
		case "b000001d": return Place.b000001d;
		case "b000001e": return Place.b000001e;
		case "b000001f": return Place.b000001f;
		case "b0000020": return Place.b0000020;
		default: return null;				
		}
	}

	public int getId() {	return id;	}
}
