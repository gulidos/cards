package ru.rik.cardsnew.domain.events;

public enum Disposition {
	ANSWERED("ANSWERED"),
	BUSY("BUSY"),
	FAILED("FAILED"),
	NO_ANSWER("NO ANSWER");
	
	private final String value;
	
	Disposition(final String disposition) {
		this.value = disposition;
	}
	
	@Override
	public String toString() {
		return value;
	}
	public String getValue() {
		return value;
	}

	 public static Disposition getEnum(String value) {
	        for(Disposition v : values())
	            if(v.getValue().equalsIgnoreCase(value)) return v;
	        throw new IllegalArgumentException();
	}
}
