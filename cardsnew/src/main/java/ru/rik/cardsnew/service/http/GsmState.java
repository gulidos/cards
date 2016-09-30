package ru.rik.cardsnew.service.http;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Builder;
import ru.rik.cardsnew.service.Futurable;
@Data
public class GsmState implements Futurable {
	
	private final Date update;
	private final GsmApp status;
	private final String operator;
	private final String sernum;
	private final int sigquality;
	private final String regstate;
	private final String iURL;
	private final String iName;
	private final String oMob;
	
	@Builder
	public GsmState(Date update, GsmApp status, String operator, String sernum, int sigquality, String regstate,
			String iURL, String iName, String oMob) {
		super();
		this.update = update;
		this.status = status;
		this.operator = operator;
		this.sernum = sernum;
		this.sigquality = sigquality;
		this.regstate = regstate;
		this.iURL = iURL;
		this.iName = iName;
		this.oMob = oMob;
	}


	public enum GsmApp {
		Unknown, Booting, Initing, Listening, Standby, Ending, Unreachable;
		
		public static GsmApp getInstance(String code) {
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


	@Override
	public Class<?> getCalss() {
		return GsmState.class;
	}
}
