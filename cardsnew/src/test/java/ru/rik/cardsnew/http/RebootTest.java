package ru.rik.cardsnew.http;

import java.io.IOException;

import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Line;
import ru.rik.cardsnew.service.http.HttpHelper;

public class RebootTest {

	public RebootTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		Channel ch = new Channel(1, 1, "ch1", Line.L6,
				new Box(1, 1, "", "172.17.1.34", 8, "", null), 
				null, null, null, true);
		HttpHelper.rebootChannel(ch);
//		System.out.println(status);
	}

}
