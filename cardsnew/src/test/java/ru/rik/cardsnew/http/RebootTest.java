package ru.rik.cardsnew.http;

import java.io.IOException;

import ru.rik.cardsnew.domain.Bank;
import ru.rik.cardsnew.domain.Box;
import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.Line;
import ru.rik.cardsnew.domain.Place;
import ru.rik.cardsnew.service.http.SimSet;

public class RebootTest {

	public RebootTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		Channel ch = new Channel(1, 1, "ch1", Line.L6,
				new Box(1, 1, "", "172.17.1.34", 8, "", null), 
				null, null, null, true);
		Card c = Card.builder()
				.place(Place.b0000010)
				.sernumber("1111111")
				.bank(new Bank(1, 1, "123", "2.1.1.1", null))
				.build();
		SimSet.post(ch, c);
//		HttpHelper.rebootChannel(ch);
//		SimSet.get(ch, null);
//		SimSet.getScomGsn(ch);
//		System.out.println(status);
	}

}
