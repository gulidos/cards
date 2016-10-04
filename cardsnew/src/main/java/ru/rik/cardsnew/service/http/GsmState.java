package ru.rik.cardsnew.service.http;

import java.io.IOException;
import java.util.Date;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import lombok.Data;
import lombok.experimental.Builder;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.service.Futurable;
@Data
public class GsmState implements Futurable {
	private final long id;
	private final String name;
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

	
	@Override
	public Class<?> getCalss() {
		return GsmState.class;
	}
}
