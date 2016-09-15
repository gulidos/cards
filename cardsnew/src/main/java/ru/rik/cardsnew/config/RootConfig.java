package ru.rik.cardsnew.config;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import ru.rik.cardsnew.domain.repo.CardsStates;
import ru.rik.cardsnew.domain.repo.Cdrs;
import ru.rik.cardsnew.service.AsyncTasks;
import ru.rik.cardsnew.service.PeriodicTasks;
import ru.rik.cardsnew.service.asterisk.AsteriskEvents;
import ru.rik.cardsnew.service.asterisk.CheckCDRTask;
import ru.rik.cardsnew.service.http.HttpHelper;

@Configuration
@EnableCaching
@EnableAsync
@EnableScheduling
@Import(ru.rik.cardsnew.db.JpaConfig.class)
// @ComponentScan(basePackages = { "ru.rik.cardsnew" }, excludeFilters = {
// @Filter(type = FilterType.CUSTOM, value = WebPackage.class) })

public class RootConfig implements SchedulingConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(RootConfig.class);

	public static class WebPackage extends RegexPatternTypeFilter {
		public WebPackage() {
			super(Pattern.compile("ru.rik.cardsnew\\.web"));
		}
	}

	@Bean public AppInitializer appInitializer()  { return new AppInitializer();	}

	@Bean public CardsStates cardsStates()  {return new CardsStates();}
	
	@Bean public HttpHelper httpHelper()  { return new HttpHelper();}
	@Bean public PeriodicTasks periodicTasks()  { return new PeriodicTasks();}
	@Bean public AsyncTasks asyncTasks()  { return new AsyncTasks();}
	@Bean public Cdrs cdrs() {return new Cdrs();}
	
	@Bean (initMethod="init") 
	public CheckCDRTask checkCDRTask() {return new CheckCDRTask();}
	
	@Bean(initMethod="start", destroyMethod="stop")
	public AsteriskEvents asteriskEvents() {
		return new AsteriskEvents();
	}
	
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
