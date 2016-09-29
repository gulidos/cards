package ru.rik.cardsnew.service.http;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@AllArgsConstructor
@EqualsAndHashCode(of={"id", "name"})
public class GsmState {
	private final long id;
	private String name;
	private volatile GsmStatus status;
	private volatile int priority;
	private Date lastUpdate;
	private String operator;
	private String sernum;
	private int sigquality;
	private String regstate;
	private String iURL;
	private String iName;
	private String oMob;
	
	
	public enum GsmStatus {
		Unknown,
		Booting,
		Initing,
		Listening, 
		Standby,
		Ending, 
		Unreachable; 
		
		public static GsmStatus getInstance(String code) {
		switch (code) {
		case "Standby":	return Standby;
		case "Listening": return Listening;
		case "Booting": return Booting;
		case "Initing":	return Initing;
		case "Ending": return Ending;	
		default: return Unknown;
		}	
	}
		
	}
}
