package ru.rik.cardsnew.domain.events;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.apache.log4j.Logger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Builder;
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class Cdr extends Event {
	static Logger log = Logger.getLogger(Cdr.class);
	public static final DecimalFormat df = new DecimalFormat("###.##");
	
	private String src;
	private String dst;
	private int billsec;
	private String trunk;
	private String regcode;
	private Disposition disposition;
	private String uniqueid;
	private String destinationChannel;
	private long channelId;
	
	@Builder
	private Cdr(String date, String src, String dst, long cardId, int billsec, String trunk, 
			String disp, String regcode, String uniqueid, String destinationChannel)
			throws ParseException {
		super(date, cardId);
		this.src = src;
		this.dst = dst;
		this.billsec = billsec;
		this.trunk = trunk;
		this.disposition = Disposition.getEnum(disp);
		this.regcode = regcode;
		this.uniqueid = uniqueid	;
		this.destinationChannel = destinationChannel;
	}

	

	public boolean isOffnet() throws Exception {
//		try {
//			String cardOper = getCardId().getName().substring(0, 3);
//			String callOper = getTrunk().substring(0, 3);
//			return !cardOper.equals(callOper);
//		} catch (Exception e) {
//			throw new Exception ("can not determine if it is offnetcall " + getCardId() + " " + e.getMessage());
//		}
		return false;
	}
	
	public int getMinOper() {
		double billsecd = (double) billsec;
		return (int) Math.ceil(billsecd / 60);
	}
	
	public double getMin() {
		double billsecd = (double) billsec;
		return billsecd / 60;
	}

	public String getMinFormatted() {
		double billsecd = (double) billsec;
		return df.format(billsecd/60);
	}
	
	private void setChannelId(){
		if (channelId == 0) {
			// FIXME implemet this!
		}
	}
	
	
}
