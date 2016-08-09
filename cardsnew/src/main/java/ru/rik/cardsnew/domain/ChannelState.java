package ru.rik.cardsnew.domain;

public class ChannelState {
	private final long channelId;
	private volatile ChStatus status;

	
	public ChannelState(Channel t) {
		channelId = t.getId();
		setStatus(ChStatus.Unknown);
	}

	public long getTrunkId() {return channelId;}
	public ChStatus getStatus() {return status;	}
	public void setStatus(ChStatus status) {this.status = status;}

	
	public enum ChStatus {
		Unknown,
		Booting,
		Initing,
		Listening, 
		Standby,
		Ending, 
		Unreachable; 
		
		
	}
	
}
