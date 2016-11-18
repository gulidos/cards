package ru.rik.cardsnew.service.telnet;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.telnet.TelnetClient;

import ru.rik.cardsnew.domain.Sms;

public class TelnetHelperMock implements TelnetHelper {
	private TelnetClient telnetClient;
	public TelnetHelperMock(TelnetClient telnetClient) {	
		this.telnetClient = telnetClient;
	}

	@Override
	public TelnetClient getConnection(String server, int port, String user, String password)
			throws IOException, SocketException {
		wait(30);
		return telnetClient;
	}

	@Override
	public ArrayList<Sms> FetchSmsFromChannel(TelnetClient telnet, int module) {
		wait(30);
		return null;
	}

	@Override
	public int deleteSms(TelnetClient telnet, List<Sms> arr) {
		wait(30);
		return 0;
	}

	@Override
	public void disconnect(TelnetClient telnet) {
		System.out.println("disconnect " + telnet);
		wait(30);
	}

	private void wait(int timeout) {
		try {Thread.sleep(timeout);} catch (InterruptedException e) {}
	}
}
