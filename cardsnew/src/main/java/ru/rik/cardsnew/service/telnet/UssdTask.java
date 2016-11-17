package ru.rik.cardsnew.service.telnet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.telnet.TelnetClient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
@NoArgsConstructor
public class UssdTask {
	@Getter private Channel ch;
	@Getter private Card card;
	@Getter @Setter private TelnetClient telnetClient;	
	@Getter @Setter private String decodedAnswer;
	
	private static final Pattern MSG_PATTERN = Pattern.compile("^\\+CUSD:\\s+(\\d)(?:,\\s*\"([^\"]*))?(?:\",\\s*(\\d+)\\s*)?\"?\r?$");
	private static final Pattern pd = Pattern.compile("(\\d)");
	private static final Pattern ps = Pattern.compile("(\\*)");
	private static final Pattern pp = Pattern.compile("(\\#)");
	
	public UssdTask(Channel ch, Card card, TelnetClient telnetClient) {
		super();
		this.ch = ch;
		this.card = card;
		this.telnetClient = telnetClient;
	}

	public String encodeReq(String req) {
		String result = null;
		Matcher m = pd.matcher(req);
		if (m.find())  result = m.replaceAll("003$1");
		System.out.println(result);
		
		m = ps.matcher(result);
		if (m.find())  result = m.replaceFirst("002a");
		System.out.println(result);

		m = pp.matcher(result);
		if (m.find())  result = m.replaceFirst("0023");
		System.out.println(result);

		return result;
	}
	
	
	// converts a PDU  string to a byte array
		public byte[] pduToBytes(String s) {
			byte[] bytes = new byte[s.length() / 2];
			for (int i = 0; i < s.length(); i += 2) {
				bytes[i / 2] = (byte) (Integer.parseInt(s.substring(i, i + 2), 16));
			}
			return bytes;
		}

		public String decodeUcs2Encoding(byte[] udhData, byte[] pduData) {
			try {
				int udhLength = ((udhData == null) ? 0 : udhData.length);
				return new String(pduData, udhLength, pduData.length - udhLength, "UTF-16");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		
//		if ($ARGV[1] ne '') {
//	        $ussd_req = request_encode ($ARGV[1]);
//	} else {
//	        if ($operator eq 'mts')  {
//	                #$ussd_req = "00230031003000300023";
//	                $ussd_req = "002a0031003000300023";
//	        } else {
//	                $ussd_req = "002a0031003000300023";
//	        }

		
//		sub request_encode {
//	        my $str = $_[0];
//	        $str =~ s/(\d)/003$1/g;
//	        $str =~ s/(\*)/002a/g;
//	        $str =~ s/(\#)/0023/g;
//	        return $str;
//	}
}
