package ru.rik.cardsnew;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.telnet.TelnetClient;

import ru.rik.cardsnew.service.telnet.TelnetHelperImpl;

public class UssdTest {

	public UssdTest() {	}

	public static void main(String[] args) throws SocketException, IOException {
		TelnetHelperImpl th = new TelnetHelperImpl();
		TelnetClient tc = th.getConnection( "192.168.5.230", 8023, "voip", "1234"); 
		String resp = th.sendUssd(tc, 2);
		System.out.println(resp);
	}
}
