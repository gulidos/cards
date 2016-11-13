package ru.rik.cardsnew.domain;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.telnet.TelnetClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.rik.cardsnew.service.telnet.SmsTask;
import ru.rik.cardsnew.service.telnet.SmsTask.Phase;
import ru.rik.cardsnew.service.telnet.TelnetHelper;

public class SmsTaskTest {
	
	public SmsTaskTest() {	}
	
	@Before 
	public void mockHelper() {
		
	}
	@Test
	public void changePhase() throws SocketException, IOException {
		TelnetHelper h = mock(TelnetHelper.class);
				
		Channel ch = Channel.builder()
				.box(Box.builder().ip("172.17.1.34").capacity(8).build())
				.line(Line.L1)
				.build();
		
		Channel pair = Channel.builder()
				.box(Box.builder().ip("172.17.1.34").capacity(8).build())
				.line(Line.L2)
				.build();
		
		SmsTask task = SmsTask.get(h, ch, null, pair, null);
		task.setTelnetClient(mock(TelnetClient.class));
		Assert.assertEquals(task.getPhase(), Phase.FetchMain);
		
		task = task.deleteMain(h);
		Assert.assertEquals(task.getPhase(), Phase.DeleteMain);
		
		task = task.fetchPair(h);
		Assert.assertEquals(task.getPhase(), Phase.FetchPair);
		
		task = task.deletePair(h);
		Assert.assertEquals(task.getPhase(), Phase.DeletePair);
		
	}

}
