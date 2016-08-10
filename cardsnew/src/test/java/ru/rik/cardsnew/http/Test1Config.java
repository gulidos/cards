package ru.rik.cardsnew.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

//import ru.rik.cardsnew.config.AppInitializer;
//import ru.rik.cardsnew.domain.repo.ChannelsStates;
//import ru.rik.cardsnew.domain.repo.TrunksStates;
//import ru.rik.cardsnew.service.http.HttpHelper;

@Configuration
public class Test1Config {

  
//	@Bean public AppInitializer appInitializer()  { return new AppInitializer();}
//	@Bean public TrunksStates trunksStats()  { return new TrunksStates();	}
//	@Bean public ChannelsStates channelsStates()  {return new ChannelsStates();}
//	@Bean public HttpHelper httpHelper()  { return new HttpHelper();}
	
	@Bean
    public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(3);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("MyExecutor-");
        executor.initialize(); 
        return executor;
    }
  
}
