package ru.rik.cardsnew.domain;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import ru.rik.cardsnew.config.Settings;
import ru.rik.cardsnew.domain.ChannelState.Status;
import ru.rik.cardsnew.service.http.GsmState;
import ru.rik.cardsnew.service.http.GsmState.GsmApp;

public class ChannelStateTest {

	
	public ChannelStateTest() {	}
	
	@Test
	public void newChanStateInFailState() {
		ChannelState state = new ChannelState(Status.Failed, new Date(0));
		Assert.assertEquals(state.getStatus(), Status.Failed);
	}
	
	@Test
	public void applyReadyStatusOnFail() {
		ChannelState s = new ChannelState(Status.Failed, new Date());
		s.setStatus(Status.Ready);
		Date now = new Date();
		Assert.assertEquals(s.getStatus(), Status.Ready);
		Assert.assertTrue(isApproxEqual(now, s.getLastStatusChange()));
		Assert.assertTrue(isApproxEqual(s.getNextGsmUpdate(),  
				Util.getNowPlusSec(Settings.NORMAL_CHECK_GSM_INTERVAL)));
	}

	@Test
	public void applyUnreachOnUnreachNotLongAgo() {
		ChannelState s = new ChannelState(Status.Unreach, Util.getNowMinusSec((int) Settings.TIME_FOR_SWITCH - 1) );
		s.setStatus(Status.Unreach);
		Assert.assertEquals(s.getStatus(), Status.Unreach);
		Assert.assertTrue(isApproxEqual(s.getNextGsmUpdate(),  
				Util.getNowPlusSec(Settings.FAILED_CHECK_GSM_INTERVAL)));
	}
	
	@Test
	public void applyUnreachOnUnreachLongAgo() {
		ChannelState s = new ChannelState(Status.Unreach,  Util.getNowMinusSec(Settings.TIME_FOR_SWITCH + 1));
		s.setStatus(Status.Unreach);
		Assert.assertEquals(s.getStatus(), Status.Unreach);
		Assert.assertTrue(isApproxEqual(s.getNextGsmUpdate(),  Util.getNowPlusSec(Settings.NORMAL_CHECK_GSM_INTERVAL)));
	}
	
	@Test
	public void inchangeViaFailedToReady() {
		ChannelState s = new ChannelState(Status.Inchange,  Util.getNowMinusSec(Settings.TIME_FOR_SWITCH - 1));
		s.setStatus(Status.Failed);
		Assert.assertEquals(s.getStatus(), Status.Inchange);
		Assert.assertTrue(isApproxEqual(s.getNextGsmUpdate(),  Util.getNowPlusSec(Settings.FAILED_CHECK_GSM_INTERVAL)));
	}
	
	@Test
	public void inchangeToFailed() {
		ChannelState s = new ChannelState(Status.Inchange,  Util.getNowMinusSec(Settings.TIME_FOR_SWITCH + 1));
		s.setStatus(Status.Failed);
		Assert.assertEquals(s.getStatus(), Status.Failed);
		Assert.assertTrue(isApproxEqual(s.getNextGsmUpdate(),  Util.getNowPlusSec(Settings.NORMAL_CHECK_GSM_INTERVAL)));
	}
	
	@Test public void applyGsStateStandby() {
		GsmState gs = GsmState.builder()
				.id(55).name("name").update(new Date())
				.status(GsmApp.Standby)
				.build();
		ChannelState s = new ChannelState(Status.Failed, new Date(0));
		s.applyGsmStatus(gs);
		Assert.assertEquals(s.getStatus(), Status.Ready);
	}
	
	@Test public void applyGsStateIniting() {
		GsmState gs = GsmState.builder()
				.id(55).name("name").update(new Date())
				.status(GsmApp.Initing)
				.build();
		ChannelState s = new ChannelState(Status.Ready, new Date(0));
		s.applyGsmStatus(gs);
		Assert.assertEquals(s.getStatus(), Status.Failed);
	}
	
	private long msecBetween(Date d1, Date d2) {
		return Math.abs(d2.getTime() - d1.getTime());
	}
	
	private boolean isApproxEqual(Date d1, Date d2) {
		return msecBetween(d1, d2) < 10;
	}
}
