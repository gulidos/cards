package ru.rik.cardsnew.service.http;

import java.io.IOException;
import java.util.Date;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Builder;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.MyState;
@Data
@EqualsAndHashCode(callSuper=false)
public class GsmState implements MyState {
	private final long id;
	private final String name;
	private final Date update;
	// TODO if Inited too long, it should be rebooted
	private final GsmApp status;  
	private final String operator;
	private final String sernum;
	private final int sigquality;
	private final String regstate;
	private final String iURL;
	private final String iName;
	private final String oMob;
	
	@Builder
	public GsmState(long id, String name, Date update, GsmApp status, String operator, String sernum, int sigquality, String regstate,
			String iURL, String iName, String oMob) {
		super();
		this.id = id;
		this.name = name;
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
	
	public static GsmState get(final Channel ch) throws IOException, IllegalAccessException {
		String nport = Integer.toString(ch.getLine().getNport());
		Connection con = HttpHelper.getCon(ch, "gsmstatus.cgi");
		
		GsmStateBuilder b = GsmState.builder();

		Document doc = con.data("nPortNum", nport).post();
		
		b.update(new Date());
		b.id(ch.getId());
		b.name(ch.getName());
		
		
		Element imob = doc.select("input[name=MSTT]").first();
		GsmApp status = GsmApp.getInstance(imob.attributes().get("value"));
		b.status(status);

		imob  = doc.select("input[name=COPS]").first();
		b.operator(imob.attributes().get("value"));
		
		imob  = doc.select("input[name=CSQ]").first();
		String v = imob.attributes().get("value");
		if ( v.length() > 0) {
			int q = Integer.parseInt(v);
			b.sigquality(q);
		}
		
		imob  = doc.select("input[name=CREG]").first();
		b.regstate(imob.attributes().get("value"));

		imob  = doc.select("input[name=CGSN]").first();
		b.sernum(imob.attributes().get("value"));
		
		imob  = doc.select("input[name=IURL]").first();
		b.iURL(imob.attributes().get("value"));
		
		imob  = doc.select("input[name=INAME]").first();
		b.iName(imob.attributes().get("value"));

		imob  = doc.select("input[name=OMOB]").first();
		b.oMob(imob.attributes().get("value"));
		
		return b.build();
	}

	

	public boolean isReady() {return status.isReady() ;}
	@Override public Class<?> getClazz() {return GsmState.class;}
	
	public enum GsmApp {
		Unknown(false), Booting(false), Initing(false),
			Inited(false), OutRing(true), InRing(true), 
			OutLst(true), InLst(true), Standby(true), 
			Ending(true), Unreachable(false);
		
		private final boolean ready;
		public boolean isReady() {	return ready;	}

		GsmApp(boolean r) {	this.ready = r;	}
		
		public static GsmApp getInstance(String str) {
			switch (str) {
				case "Standby":	return Standby;
				case "Mobile: Listen": return OutLst;
				case "LAN: Listen": return InLst;
				case "Mobile: ringback": return OutRing;
				case "LAN: ringback": return InRing;
				case "Booting": return Booting;
				case "Initing":	return Initing;
				case "Inited":	return Inited;
				case "Ending": return Ending;	
				default: return Unknown;
			}	
		}
	}

	@Override	public void setId(long id) {	}

	@Override 	public void setName(String name) {	}
}
