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

import ru.rik.cardsnew.db.ChannelRepo;
import ru.rik.cardsnew.db.ChannelRepoImpl;
import ru.rik.cardsnew.domain.ChannelState;
import ru.rik.cardsnew.domain.ChannelState.Status;
import ru.rik.cardsnew.domain.State;
import ru.rik.cardsnew.service.http.GsmState;

public class TaskCompleter implements Runnable{
	private static final Logger logger = LoggerFactory.getLogger(TaskCompleter.class);		

	private final CompletionService<State> completionServ;
	private final ThreadPoolTaskExecutor executor;
	private final ConcurrentMap<Future<State>, State> map;


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
				
			} catch (InterruptedException e) {
				logger.info("interrupted");
				Thread.currentThread().interrupt();
			} catch (ExecutionException e) {
				execExceptionHandler(f, e);
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
				logger.debug("channel {} is unreachable", chState.getName());
			}	
		} else 
			logger.error(e.getMessage(), e);
	}
	
	
	private void applyGsmState(GsmState g) {
		logger.debug(g.toString());
		
		ChannelRepo repo = ChannelRepoImpl.get();
		ChannelState st = repo.findStateById(g.getId());
		st.applyGsmStatu(g);
	}
	

	
}
