package ru.rik.cardsnew;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ru.rik.cardsnew.config.AppInitializer;
import ru.rik.cardsnew.db.JpaConfig;
import ru.rik.cardsnew.domain.repo.CardsStates;
import ru.rik.cardsnew.service.AsyncTasks;
import ru.rik.cardsnew.service.PeriodicTasks;
import ru.rik.cardsnew.service.asterisk.AsteriskEvents;
import ru.rik.cardsnew.service.http.HttpHelper;

@Configuration
@ComponentScan(basePackages="ru.rik.cardsnew.db")
@EnableTransactionManagement
@EnableCaching
@Import(JpaConfig.class)

public class TestConfig {
	
  
	@Bean public AppInitializer appInitializer()  { return new AppInitializer();	}
	@Bean public CardsStates cardsStates()  {return new CardsStates();}
	
	@Bean public HttpHelper httpHelper()  { return new HttpHelper();}
	@Bean public PeriodicTasks periodicTasks()  { return new PeriodicTasks();}
	@Bean public AsyncTasks asyncTasks()  { return new AsyncTasks();}
	
	@Bean(initMethod="start", destroyMethod="stop")
	public AsteriskEvents asteriskEvents() {
		return new AsteriskEvents();
	}
	
	@Bean
    public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(7);
        executor.setMaxPoolSize(42);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("MyExecutor-");
        executor.initialize();
        return executor;
    }

  @Configuration
  @EnableTransactionManagement
  public static class TransactionConfig {


	@Autowired
    private EntityManagerFactory emf;
    @Autowired
    private DataSource dataSource;

    @Bean
    public PlatformTransactionManager transactionManager() {
      JpaTransactionManager transactionManager = new JpaTransactionManager();
      transactionManager.setEntityManagerFactory(emf);
      transactionManager.setDataSource(dataSource);
      return transactionManager;
    }    
  }
  
}
