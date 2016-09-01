package ru.rik.cardsnew.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import ru.rik.cardsnew.service.AsyncTasks;
import ru.rik.cardsnew.service.PeriodicTasks;
import ru.rik.cardsnew.service.http.HttpHelper;


@Configuration
@EnableAsync
@EnableScheduling
public class Test1Config implements SchedulingConfigurer {

  
//	@Bean public AppInitializer appInitializer()  { return new AppInitializer();}
//	@Bean public TrunksStates trunksStats()  { return new TrunksStates();	}
//	@Bean public ChannelsStates channelsStates()  {return new ChannelsStates();}
	@Bean public HttpHelper httpHelper()  { return new HttpHelper();}
	@Bean public PeriodicTasks periodicTasks()  { return new PeriodicTasks();}
	@Bean public AsyncTasks asyncTasks()  { return new AsyncTasks();}
	
	
	@Bean
    public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(3);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("MyExecutor-");
        executor.initialize(); 
        return executor;
    }
	
	@Bean(destroyMethod="shutdown")
    public TaskExecutor taskSheduleExecutor() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setThreadNamePrefix("MyScheduler-");
		scheduler.setPoolSize(5);
        return scheduler;
    }
	

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskSheduleExecutor());
	}
	
}
