package ru.rik.cardsnew.service.telnet;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.telnet.TelnetClient;

import ru.rik.cardsnew.domain.Sms;

public interface TelnetHelper {

	TelnetClient getConnection(String server, int port, String user, String password)
			throws IOException, SocketException;

	ArrayList<Sms> FetchSmsFromChannel(TelnetClient telnet, int module);

	int deleteSms(TelnetClient telnet, List<Sms> arr);

	void disconnect(TelnetClient telnet);

}