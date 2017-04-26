package ru.rik.cardsnew.asterisk;

import java.io.IOException;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.GetVarAction;
import org.asteriskjava.manager.action.SipShowPeerAction;
import org.asteriskjava.manager.event.CdrEvent;
import org.asteriskjava.manager.event.CoreShowChannelEvent;
import org.asteriskjava.manager.event.CoreShowChannelsCompleteEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.PeerEntryEvent;
import org.asteriskjava.manager.response.ManagerResponse;
import org.asteriskjava.manager.response.SipShowPeerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class HelloEvents implements ManagerEventListener {
	private static final Logger log = LoggerFactory.getLogger(HelloEvents.class);		

	

	private ManagerConnection managerConnection;

	public HelloEvents() throws IOException {
		//ManagerConnectionFactory factory = new ManagerConnectionFactory("178.217.182.245", 51038, "manager", "PaSsWoRd151201");
		ManagerConnectionFactory factory = new ManagerConnectionFactory("localhost",  "myasterisk", "mycode");

		this.managerConnection = factory.createManagerConnection();
	}

	public void run() throws IOException, AuthenticationFailedException, TimeoutException, InterruptedException {
		managerConnection.addEventListener(this);
		managerConnection.login();
//		managerConnection.sendAction(new CoreShowChannelsAction());
//
//		managerConnection.sendAction(new SipPeersAction());
		
//		Thread.sleep(1001);
		 GetVarAction getVarAction = new GetVarAction("DEVICE_STATE(SIP/mts80)");
		  ManagerResponse response = managerConnection.sendAction(getVarAction);
		  String value = response.getAttribute("Value");
		  System.out.println("MY_VAR on "  + " is " + value);
		  
//		  Thread.sleep(1001);
		ManagerResponse mr =  managerConnection.sendAction(new SipShowPeerAction("bln74"));
		SipShowPeerResponse sipResp = (SipShowPeerResponse) mr;
		log.debug("!!! resp" + sipResp.toString() + " status: " + sipResp.getStatus());


	managerConnection.logoff();
	}


	
	 public void onManagerEvent(ManagerEvent event) {
//		 log.debug(event.toString());
		if (event instanceof CdrEvent) {
			
			CdrEvent cdrevent = (CdrEvent) event;
			log.info("AnswerTime: " + cdrevent.getAnswerTime() +
					 " AnswerTimeAsDate: " + cdrevent.getAnswerTimeAsDate() +
					 " StartTime: " + cdrevent.getStartTime() +
					 " StartTimeAsDate: " + cdrevent.getStartTimeAsDate() +
					 " Src: " + cdrevent.getSrc() +
					 " Destination: " + cdrevent.getDestination() +
					 " Disposition: " + cdrevent.getDisposition() +
					 " BillableSeconds: " + cdrevent.getBillableSeconds() +
					 " Duration: " + cdrevent.getDuration() +
					 " UserField: " + cdrevent.getUserField() + 
					 " Trunk: " + cdrevent.getTrunk() +
					 " gateip: " + cdrevent.getGateip() +
					 " Regcode: " + cdrevent.getRegcode()
					 );
		} 
//		else if (event instanceof NewStateEvent) {
//			NewStateEvent psevent = (NewStateEvent) event;
//			log.info("NewStateEvent: " + " Channel: " + psevent.getChannel() + " ChannelState: "
//					+ psevent.getChannelState() + " CallerIdNum: " + psevent.getCallerIdNum() + " CallerIdName: "
//					+ psevent.getCallerIdName() + " ChannelStateDesc: " + psevent.getChannelStateDesc() + " UniqueId: "
//					+ psevent.getUniqueId());
//		} 
//		else if (event instanceof HangupEvent) {
//			HangupEvent psevent = (HangupEvent) event;
//			log.info("HangupEvent: " + " CallerIdName: " + psevent.getCallerIdName() + " CauseTxt: "
//					+ psevent.getCauseTxt() + " CallerIdNum: " + psevent.getCallerIdNum() + " Channel: "
//					+ psevent.getChannel() + " UniqueId: " + psevent.getUniqueId());
//		}

//	         else if (event instanceof NewChannelEvent) {
//	        	 NewChannelEvent psevent = (NewChannelEvent) event;
//	        	 log.info("NewChannelEvent: " + 
//	        			 " CallerIdName: " + psevent.getCallerIdName() + 
//	        			 " CallerIdNum: " + psevent.getCallerIdNum() + 
//	        			 " Channel: " + psevent.getChannel()+ 
//	        			 " ChannelState: " + psevent.getChannelState()+
//	        			 " ChannelStateDesc: " + psevent.getChannelStateDesc()+ 
//	        			 " Exten: " + psevent.getExten() +
//	        			 " Timestamp: " + psevent.getTimestamp() +
//	        			 " UniqueId: " + psevent.getUniqueId()
//	        			 ); 
//	         }
//	         
//	        	 else if (event instanceof DialEvent) {
//	        		 DialEvent psevent = (DialEvent) event;
//	        		 log.info("DialEvent: " +
//	        			 " Channel: " +psevent.getChannel() + 
//	        			 " DialStatus: " +psevent.getDialStatus() + 
//	        			 " CallerIdNum: " +psevent.getCallerIdNum() + 
//	        			 " DialString: " +psevent.getDialString() + 
//	        			 " Destination: " +psevent.getDestination() +
//	        			 " DestUniqueId: " +psevent.getDestUniqueId() +
//	        			 " SubEvent: " +psevent.getSubEvent() +
//	        			 " UniqueId: " +psevent.getUniqueId()
//	        			 ); 
//	         }
	         
	         else if (event instanceof CoreShowChannelEvent) {
	        	 CoreShowChannelEvent psevent = (CoreShowChannelEvent) event;
	        	 log.info("BridgedChannel: " + 
	        			 psevent.getBridgedChannel() + " ChannelState: " +
	        			 psevent.getChannelState() + " Channelstatedesc: " +
	        			 psevent.getChannelstatedesc() + " Applicationdata: " +
	        			 psevent.getApplicationdata()+ " Channel: " + 
	        			 psevent.getChannel()+ "  Duration: " +
	        			 psevent.getDuration() + " Uniqueid: " +
	        			 psevent.getUniqueid()
	        			 ); 
	         }	 
	 
	         else if (event instanceof CoreShowChannelsCompleteEvent) {
	        	 CoreShowChannelsCompleteEvent psevent = (CoreShowChannelsCompleteEvent) event;
	        	 log.info( "CoreShowChannelsCompleteEvent: " +
	        			 psevent.getEventlist() + " " +
	        			 psevent.getListitems() + " " +
	        			 psevent.getTimestamp()
	        			 ); 
	         }
	         else if (event instanceof PeerEntryEvent) {
	        	 PeerEntryEvent psevent = (PeerEntryEvent) event;
	        	 log.info( "PeerEntryEvent: " +
	        			 psevent.getObjectName() + " " +
	        			 psevent.getIpAddress() + " " +
	        			 psevent.getStatus() + " " +
	        			 psevent.getDynamic() + " " +
	        			 psevent.getObjectUserName() + " " +
	        			 psevent.getPort()
	        			 
	        			 ); 
	         }
//	         else if (event instanceof RegistryEvent) {
//	        	 RegistryEvent psevent = (RegistryEvent) event;
//	        	 log.info("RegistryEvent: " + 
//	        			 psevent.getUsername() + " " +
//	        			 psevent.getStatus() + " " +
//	        			 psevent.getChannelType() + " " +
//	        			 psevent.getPrivilege()
//	        			 ); 
//	         }	
//	        
		
		
		
	 }
		public static void main(String[] args) throws Exception {
			HelloEvents helloEvents;

			helloEvents = new HelloEvents();
			helloEvents.run();
		
	    }
}