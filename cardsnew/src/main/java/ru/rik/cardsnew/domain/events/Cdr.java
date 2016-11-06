package ru.rik.cardsnew.domain.events;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Builder;
import ru.rik.cardsnew.domain.Card;
@Data
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=true)
public class Cdr extends Event {
	public static final DecimalFormat df = new DecimalFormat("###.##");
	private static Pattern p = Pattern.compile("^SIP\\/(.*)-.*$");
	
	private String src;
	private String dst;
	private int billsec;
	private String trunk;
	private String regcode;
	private Disposition disposition;
	private String uniqueid;
	private long channelId;
	
	@Builder
	private Cdr(Date date, String src, String dst, long cardId, int billsec, String trunk, 
			String disp, String regcode, String uniqueid, long channelId)
			throws ParseException {
		super(date, cardId);
		this.src = src;
		this.dst = dst;
		this.billsec = billsec;
		this.trunk = trunk;
		this.disposition = Disposition.getEnum(disp);
		this.regcode = regcode;
		this.uniqueid = uniqueid;
		this.channelId = channelId;
	}

	
	
	public int getMinOper() {
		double billsecd = (double) billsec;
		return (int) Math.ceil(billsecd / 60);
	}
	
	public  double getMin() {
		double billsecd = (double) billsec;
		return billsecd / 60;
	}

	public String getMinFormatted() {
		double billsecd = (double) billsec;
		return df.format(billsecd/60);
	}
	
	public static String parseChannel(String s) {
		if (s == null )
			throw new IllegalArgumentException("String for parsing can not be null");
		Matcher m = p.matcher(s);
		if (m.matches())
			return m.group(1);
		else
			return "";
	}
	
	public boolean isOffnet(Card c)  {
//		Card c = CardRepoImpl.get().findById(cardId);
		String cardOper = c.getName().substring(0, 3);
		String callOper = getTrunk().substring(0, 3);
		return !cardOper.equals(callOper);
	}
	
	static public void main(String[] args) {
		String dsstch = "SIP/mgf37-003907c3";
		System.out.println(parseChannel(dsstch));
	}
	
	
}
