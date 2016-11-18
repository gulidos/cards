package ru.rik.cardsnew.service.telnet;

import java.io.IOException;
import java.util.List;

import org.apache.commons.net.telnet.TelnetClient;

import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Sms;

public class GetSms {

	public GetSms() {}
	public static void main(String[] args) throws IOException {

		try {
			Channel ch = new Channel();
			System.out.print("Читаем sms из " + ch.getName() + " " + " ...  ");
			TelnetHelper t = new TelnetHelperImpl();
			TelnetClient tc  = t.getConnection("172.17.1.34" , 8123, "voip", "1234");
//			TelnetClient tc  = t.getConnection("192.168.5.206" , 8223, "voip", "1234");
			for (int i = 1; i <= 2; i++) {
				System.out.println("module " + i);
				List<Sms> allsms = t.FetchSmsFromChannel(tc, i);
				if (allsms.size() > 0) {
					for (Sms sms : allsms) {
						System.out.println(sms.getDate().getTime() + " From: " + sms.getOrigAddress() + " Sms: "
								+ sms.getEncodedmsg());
					}
					t.deleteSms(tc, allsms);
				} else
					System.out.println("there is nothing ");
				if (i ==1)
					Thread.sleep(1000);

			}
			
			t.disconnect(tc);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
