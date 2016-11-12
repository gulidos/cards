package ru.rik.cardsnew.service;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

import ru.rik.cardsnew.db.BankRepo;
import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.domain.BankState;
import ru.rik.cardsnew.domain.Channel;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.ChannelState.Status;
import ru.rik.cardsnew.domain.State;
import ru.rik.cardsnew.service.http.BankStatus;
import ru.rik.cardsnew.service.http.GsmState;
import ru.rik.cardsnew.service.http.SimSet;
import ru.rik.cardsnew.service.telnet.SmsTask;
import ru.rik.cardsnew.service.telnet.TelnetHelper;

public class TaskCompleter implements Runnable{
	private static final Logger logger = LoggerFactory.getLogger(TaskCompleter.class);		

	private final CompletionService<State> completionServ;
	private final ThreadPoolTaskExecutor executor;
	private final ConcurrentMap<Future<State>, State> map;
	@Autowired private ChannelRepo chans;
	@Autowired private BankRepo banks;
	@Autowired private TelnetHelper telnetHandler;

	@Autowired
	public TaskCompleter(CompletionService<State> completionService,
			ThreadPoolTaskExecutor taskExecutor ) {
		this.completionServ = completionService;
		this.executor = taskExecutor;
		this.map = new ConcurrentHashMap<>();
	}

	
	public void start() {
		executor.submit(this);
	}

	public Future<State> addTask(Callable<State> task, State st ) {
		Future<State> f = completionServ.submit(task);
		map.putIfAbsent(f, st);
		return f;
	}
	
	
	@Override
	public void run() {
		Future<State> f = null;
		while (!Thread.currentThread().isInterrupted()) {
			try {
				f = completionServ.take();
				State result = f.get();
				map.remove(f);
				
				if (result.getClazz() == GsmState.class)				
					applyGsmState((GsmState) result);
				else if (result.getClazz() == SimSet.class) {
					applySimSet(result);
				} 
				else if (result.getClazz() == BankStatus.class) {
					applyBankStatus((BankStatus) result);
				} 
				else if (result.getClazz() == Switcher.class) {
					Switcher sw = (Switcher) result;
					logger.debug("installing in channel {} card {}", sw.getName(), sw.getCardName());
				}
				else if (result.getClazz() == SmsTask.class) {
					handleSms((SmsTask) result);
				}
			} catch (InterruptedException e) {
				logger.info("interrupted");
				Thread.currentThread().interrupt();
			} catch (ExecutionException e) {
				execExceptionHandler(f, e);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}


	/**
     * Handles all the exceptions during the task's execution
     */
	private void execExceptionHandler(Future<State> f, ExecutionException e) {
		Throwable cause = e.getCause();
		if (cause instanceof SocketTimeoutException || cause instanceof ConnectException) {
			State st = map.remove(f);
			if (st.getClazz() == ChannelState.class) {
				ChannelState chState = (ChannelState) st;
				chState.setStatus(Status.Unreach);
			} else if (st.getClazz() == BankState.class) {
				BankState bState = (BankState) st;
				bState.setAvailable(false);
			}
		} else 
			logger.error(e.getMessage(), e);
	}
	
	
	private void applyGsmState(GsmState g) {
//		logger.debug(g.toString());
		
		ChannelState st = chans.findStateById(g.getId());
		st.applyGsmStatus(g);
	}
	

	private void applySimSet(State result) {
		SimSet ss = (SimSet) result;
		ChannelState st = chans.findStateById(ss.getId());
		Assert.notNull(st, "SimSet applying. Can not find ChanelState " + ss.getName());
		st.applySimSet(ss);
		
		SimSet ssPair = ss.getPairData();
		if (ssPair != null) {
			ChannelState stPair = chans.findStateById(ssPair.getId());
			Assert.notNull(stPair, "SimSet applying. Can not find ChanelState " + ssPair.getName());
			stPair.applySimSet(ssPair);
		}
	}
	
	
	private void applyBankStatus(BankStatus g) {		
		BankState st = banks.findStateById(g.getId());
		st.applyBankStatus(g);
	}
	
	
	private void handleSms(SmsTask smsTask) {
		Channel ch = smsTask.getCh();
		Channel pair = smsTask.getPair();
		switch (smsTask.getPhase()) {
		case FetchMain:
			System.out.println(ch.getName() + " got smses: " + smsTask.getSmslist());
			if (smsTask.getSmslist().size() > 0) {
				Callable<State> getsms = () -> smsTask.deleteMain(telnetHandler);
				addTask(getsms, ch.getState(chans));
			}	
			break;
		case DeleteMain:	
			System.out.println(ch.getName() + " delete main smses completed: ");
			if (smsTask.getPair() != null) {
				Callable<State> getsms = () -> smsTask.fetchPair(telnetHandler);
				addTask(getsms, ch.getState(chans));
			}	
		case FetchPair:	
			System.out.println(pair.getName() + " got pair smses: " + smsTask.getSmslist());
			if (smsTask.getSmslist().size() > 0) {
				Callable<State> getsms = () -> smsTask.deletePair(telnetHandler);
				addTask(getsms, ch.getState(chans));
			}
		case DeletePair:	
			System.out.println(pair.getName() + " delete pair smses completed: ");
			
		default:
			break;
		}
		
	}

	
}
