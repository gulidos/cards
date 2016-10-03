package ru.rik.cardsnew.service;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import ru.rik.cardsnew.service.http.GsmState;

public class TaskCompleter implements Runnable{
	private final CompletionService<Futurable> cs;
	private final ThreadPoolTaskExecutor te;
//	private final ConcurrentMap

	@Autowired
	public TaskCompleter(CompletionService<Futurable> completionService,
			ThreadPoolTaskExecutor taskExecutor ) {
		this.cs = completionService;
		this.te = taskExecutor;
	}

	public void start() {
		te.submit(this);
	}

	@Override
	public void run() {
		Future<Futurable> f = null;
		try {
			f = cs.take();
			Futurable result = f.get();
			Class<?> c = result.getCalss();
			GsmState g = (GsmState) result;
			System.out.println(map.get(f) + " status: " + g);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
	}
}
