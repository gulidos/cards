// по мотивам http://letrungthang.blogspot.ru/2011/12/telnet-in-java.html
package ru.rik.cardsnew.service.telnet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.attention.sms.pdu.SMSDeliver;
import com.googlecode.attention.util.Convert;

import ru.rik.cardsnew.domain.Sms;

public class TelnetHelperImpl implements TelnetHelper {
	private static final Logger logger = LoggerFactory.getLogger(TelnetHelperImpl.class);		
	
	private final Pattern beginP = Pattern.compile("^\\s*(\\d{1,2}),\\d{1,2},,\\d{1,3}\\s*(07.*)\\s*0*\\s*", Pattern.MULTILINE);
	public final Pattern free = Pattern.compile("\\s*^\\s*.*free\\.\\s*\\]", Pattern.DOTALL);
	private final int waitTime = 500; 
	// private static final String NOT_SMS_START = "^\\+CMGL.*|^0$";

	public static void main(String[] args) {
		String str = "\n module 2: free.\n]";
		TelnetHelperImpl t = new TelnetHelperImpl();
//		Matcher matcher = t.free.matcher(str);
		if (t.free.matcher(str).matches()) {
			System.out.println("matches");
		}
	}
	public TelnetHelperImpl() {	}
	


	public TelnetClient getConnection (String server, int port, String user, String password) throws IOException, SocketException {
		TelnetClient telnet = new TelnetClient();
			try {
				telnet.setReceiveBufferSize(10000);
				telnet.addOptionHandler(new TerminalTypeOptionHandler("VT100", false, false, true, false));
				telnet.setReaderThread(true); 
				telnet.addOptionHandler(new EchoOptionHandler(true, false, true, false));
				telnet.addOptionHandler(new SuppressGAOptionHandler(true, true, true, true));
				telnet.setConnectTimeout(10000);
				
			} catch (InvalidTelnetOptionException e) {
				logger.error(e.getMessage(), e);
			}
			telnet.connect(server, port);
			
			wait(1000);
			
			readUntil(telnet, "username: ", 10);
			write(telnet, user); wait(waitTime);
			
			readUntil(telnet, "password: *", 10);
			write(telnet, password); wait(waitTime);
			readUntil(telnet, "]", 10);
			telnet.setSoTimeout(70000);
			return telnet;
	}

	
	
	public ArrayList<Sms> FetchSmsFromChannel(TelnetClient telnet, int module)  {
		ArrayList<Sms> result = new ArrayList<>(); 
		
		String state = sendCmd(telnet, "state" + module, "]", 10);
		if (!free.matcher(state).matches()) {
			sendCmd(telnet, "\u0018", "]", 10);
			return result;    // channel isn't ready
		}	
		
		sendCmd(telnet, "module" + module, "got!! press 'ctrl-x' to release module " + module + ".", 10);
		sendCmd(telnet, "AT+CMGF=0", "\n0\r\n", 10);
		String resp = sendCmd(telnet, "AT+CMGL=4", "\r\n0\r\n", 10);
		
		if (resp != null && resp.trim().length() > 5) 
			for (String str : resp.split("\\+CMGL: ")) {
				Matcher matcher = beginP.matcher(str);
				if (matcher.matches()) {
					Sms sms = new Sms();
					sms.setNum(Integer.parseInt(matcher.group(1)));
					String msgstr = matcher.group(2);
					sms.setDecodedmsg(msgstr);
					int smscInfoLength = Convert.hexToInt(msgstr.substring(0, 2));
					msgstr = msgstr.substring(2);
					msgstr = msgstr.substring(smscInfoLength * 2);
					
					SMSDeliver decoder = new SMSDeliver(msgstr);
					decoder.decode();
					sms.setEncodedmsg(decoder.getMessage());
					sms.setOrigAddress(decoder.getOriginatingAddress().getNumber());
					sms.setDate(decoder.getServiceCentreTimeStamp().getDate().getTime());
					result.add(sms);
//					System.out.println("sms:" + sms.toString());
				}
			}
		if(result.size()==0)
			sendCmd(telnet, "\u0018", "]", 10);
		return result;

	}
	
	@Override
	public String sendUssd(TelnetClient telnet, int module, String encodedReq) {
		String state = sendCmd(telnet, "state" + module, "]", 10);
		if (!free.matcher(state).matches()) {
			sendCmd(telnet, "\u0018", "]", 1000);
			return null;
		}	
		sendCmd(telnet, "module" + module, "got!! press 'ctrl-x' to release module " + module + ".", 10);
		sendCmd(telnet, "AT+CSCS=\"UCS2\"", "\n0\r\n", 10);
		String encodedResp = sendCmd(telnet, "AT+CUSD=1,\"" + encodedReq + "\"", "\n0\r\n", 3000);
		return encodedResp;
	}

	
	public int deleteSms(TelnetClient telnet, List<Sms> arr) {
		int i = 0;
		for (Sms sms : arr) {
			sendCmd(telnet, "AT+CMGD=" + sms.getNum(), "0\r\n", 10);
			i++;
		}
		arr.clear();
		sendCmd(telnet, "\u0018", "]", 3000);
		return i;
	}

	private String readUntil(TelnetClient telnet, String pattern, int timeout)  {
		wait(timeout);
		InputStream in = telnet.getInputStream();
		StringBuffer sb = null;
		int numRead = 0;
		try {
			char lastChar = pattern.charAt(pattern.length() - 1);
			sb = new StringBuffer();
			StringBuffer sbI = new StringBuffer();
			int i = in.read();
			char ch = (char) i;

			while (true) {
				numRead++;
				sb.append(ch);
				sbI.append(i).append(" ");

				if (ch == lastChar) {
					if ( numRead == 3 && pattern.endsWith("0\r\n")) {	
						pattern = "0\r\n";
					}	
					if (sb.toString().endsWith(pattern)) 
						break;
				}

				ch = (char) in.read();
//				System.out.print(ch);
			}
		} catch (SocketTimeoutException et) {
			logger.error(et.getMessage(), et);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return sb != null ? sb.toString() : "";
	}

	private void wait(int timeout) {
		try {Thread.sleep(timeout);} catch (InterruptedException e) {logger.error(e.getMessage(), e);}
	}

	private void write(TelnetClient telnet, String value) {
		try {
			PrintStream out = new PrintStream(telnet.getOutputStream());
			out.print(value + "\r\n");
			out.flush();
//			System.out.println("send command: " + value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private String sendCmd(TelnetClient telnet, String command, String prompt, int timeout) {
//		System.out.println("send command" + command);
		try {
			write(telnet, command);
			return readUntil(telnet, prompt, timeout);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	
	public void disconnect(TelnetClient telnet) {
		try {
			telnet.disconnect();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
