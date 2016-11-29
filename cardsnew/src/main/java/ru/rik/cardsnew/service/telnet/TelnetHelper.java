package ru.rik.cardsnew.service.telnet;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.util.List;

import org.apache.commons.net.telnet.TelnetClient;

import ru.rik.cardsnew.domain.Sms;

public interface TelnetHelper {

	TelnetClient getConnection(String server, int port, String user, String password)
			throws IOException, SocketException;

	List<Sms> FetchSmsFromChannel(TelnetClient telnet, int module);

	int deleteSms(TelnetClient telnet, List<Sms> arr);

	void disconnect(TelnetClient telnet);

	String sendUssd(TelnetClient telnet, int module, String encodedReq) throws ConnectException;

}