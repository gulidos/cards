package ru.rik.cardsnew.service.telnet;

import java.io.IOException;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.telnet.TelnetClient;
import org.junit.Assert;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Oper;
@NoArgsConstructor
public class UssdTask {
	@Getter @Setter private Channel ch;
	@Getter @Setter private Card card;
	@Getter @Setter private TelnetClient telnetClient;
	@Getter @Setter private String encodedResp;
	@Getter @Setter private String decodedResp;
	
	private static final Pattern MSG_PATTERN = Pattern.compile("^\\+CUSD:\\s+(\\d)(?:,\\s*\"([^\"]*))?(?:\",\\s*(\\d+)\\s*)?\"?\r?$", Pattern.MULTILINE);
	private static final Pattern pd = Pattern.compile("(\\d)");
	private static final Pattern ps = Pattern.compile("(\\*)");
	private static final Pattern pp = Pattern.compile("(\\#)");
	
	private static final Pattern greenBalance = Pattern.compile("(^\\-*\\d{1,4}[.,]\\d\\d)(р.*)", Pattern.MULTILINE);
	private static final Pattern yellowBalance = 
//			Pattern.compile("^(Баланс.?|Минус.?|Balans.?|Balance.?|Minus.?).*"
			Pattern.compile("^\\s*(Баланс.?|Минус.?|Balans.?|Balance.?|Minus.?)\\s*(\\d{1,4}[.,]\\d\\d)(\\s*р*.*)"
					, Pattern.MULTILINE);

	
	public UssdTask(Channel ch, Card card, TelnetClient telnetClient) {
		super();
		this.ch = ch;
		this.card = card;
		this.telnetClient = telnetClient;
	}

	
	public static UssdTask get(TelnetHelper h, Channel ch, Card card) throws SocketException, IOException {
		Assert.assertNotNull(ch);
		Assert.assertNotNull(card);
		TelnetClient tc  = h.getConnection(ch.getBox().getIp() ,
				ch.getLine().getTelnetport(),
				Box.DEF_USER, Box.DEF_PASSWORD);
		UssdTask task = new UssdTask(ch, card, tc);
		
		return task;
	}
	
	public UssdTask sendUssd(TelnetHelper h, String request) {
		String encodedReq = encodeReq(request);
		encodedResp = h.sendUssd(telnetClient, ch.getLine().getNport() + 1, encodedReq);
		System.out.println("encodedResp: " + encodedResp);
		return this;	
	}
	
	
	public String getDecoded() {
		if (encodedResp == null)
			return "";
		String str = null;
		Matcher m = MSG_PATTERN.matcher(encodedResp);
		if (m.find()) {
			System.out.println(m.group(1));
			System.out.println(m.group(2));
			str = m.group(2);
		} 
		
		byte[] responded = pduToBytes(str);
		return decodeUcs2Encoding(null, responded);
	}	
		
	
	
	public String encodeReq(String req) {
		String result = null;
		Matcher m = pd.matcher(req);
		if (m.find())  result = m.replaceAll("003$1");
		
		m = ps.matcher(result);
		if (m.find())  result = m.replaceFirst("002a");

		m = pp.matcher(result);
		if (m.find())  result = m.replaceFirst("0023");

		return result;
	}
	
	public float getBalance() {
		float balance = 0;
		String b = null;
		if (ch.getGroup().getOper() == Oper.GREEN) {
			Matcher m = greenBalance.matcher(encodedResp);
			if (m.find())  b = m.group(1);
		} else {
			Matcher m = yellowBalance.matcher(encodedResp);
			
			if (m.find()) {
				b = m.group(2);
				System.out.println("matched " + m.group(1) + " " + m.group(2));
			}
		}
		balance = Float.parseFloat(b);
		System.out.println("balance: " + b);
		return balance;
	}
	
	// converts a PDU  string to a byte array
		private byte[] pduToBytes(String s) {
			byte[] bytes = new byte[s.length() / 2];
			for (int i = 0; i < s.length(); i += 2) {
				bytes[i / 2] = (byte) (Integer.parseInt(s.substring(i, i + 2), 16));
			}
			return bytes;
		}

		private String decodeUcs2Encoding(byte[] udhData, byte[] pduData) {
			try {
				int udhLength = ((udhData == null) ? 0 : udhData.length);
				return new String(pduData, udhLength, pduData.length - udhLength, "UTF-16");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
}
