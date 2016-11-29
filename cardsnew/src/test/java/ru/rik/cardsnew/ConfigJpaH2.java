package ru.rik.cardsnew;

import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ThreadFactory;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ru.rik.cardsnew.config.RootConfig.MyUncaughtExceptionHandler;
import ru.rik.cardsnew.domain.State;
import ru.rik.cardsnew.domain.repo.CardsStates;
import ru.rik.cardsnew.domain.repo.Cdrs;
import ru.rik.cardsnew.service.PeriodicTasks;
import ru.rik.cardsnew.service.SwitchTask;
import ru.rik.cardsnew.service.TaskCompleter;
import ru.rik.cardsnew.service.asterisk.AsteriskEvents;
import ru.rik.cardsnew.service.http.HttpHelper;
import ru.rik.cardsnew.service.telnet.TelnetHelper;
import ru.rik.cardsnew.service.telnet.TelnetHelperImpl;

@EnableTransactionManagement
@EnableCaching
@Configuration
@ComponentScan(basePackages = { "ru.rik.cardsnew.db" })
@EnableJpaRepositories(basePackages="ru.rik.cardsnew.db")
public class ConfigJpaH2 {

	public ConfigJpaH2() {}
	
	public DataSource dataSourceTarget() {
		EmbeddedDatabaseBuilder edb = new EmbeddedDatabaseBuilder();
		edb.setType(EmbeddedDatabaseType.H2);
		edb.addScript("schema.sql");
		edb.addScript("test-data.sql");
		EmbeddedDatabase embeddedDatabase = edb.build();
		return embeddedDatabase;
	}

	@Bean
	public DataSource dataSource() {
		LazyConnectionDataSourceProxy dataSourceProxy = new LazyConnectionDataSourceProxy();
		dataSourceProxy.setTargetDataSource(dataSourceTarget());
		return dataSourceProxy;
	}

	private Map<String, ?> jpaProperties() {
		Map<String, String> jpaPropertiesMap = new HashMap<String, String>();
		jpaPropertiesMap.put("hibernate.hbm2ddl.auto", "update");
		jpaPropertiesMap.put("hibernate.cache.use_second_level_cache", "true");
		jpaPropertiesMap.put("hibernate.cache.use_query_cache", "true");
		jpaPropertiesMap.put("hibernate.cache.region.factory_class",
				"org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
		jpaPropertiesMap.put("net.sf.ehcache.configurationResourceName", "ehcache.xml");
		jpaPropertiesMap.put("hibernate.cache.use_structured_entries", "false");
		jpaPropertiesMap.put("hibernate.generate_statistics", "true");
		jpaPropertiesMap.put("hibernate.cache.auto_evict_collection_cache", "true"); 
		return jpaPropertiesMap;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
			JpaVendorAdapter jpaVendorAdapter) {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource);
		emf.setPersistenceUnitName("cards");
		emf.setJpaVendorAdapter(jpaVendorAdapter);
		emf.setPackagesToScan("ru.rik.cardsnew.domain");
		emf.setJpaPropertyMap(jpaProperties());
		return emf;
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.H2);
		adapter.setShowSql(false);
		adapter.setGenerateDdl(false);
		adapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
		return adapter;
	}

	@Bean
	public BeanPostProcessor persistenceTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean
	public CacheManager cacheManager() {
		return new EhCacheCacheManager(ehCacheCacheManager().getObject());
	}

	@Bean
	public EhCacheManagerFactoryBean ehCacheCacheManager() {
		EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
		cmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));
		cmfb.setShared(true);
		return cmfb;
	}
	
	@Bean(initMethod = "init")
	public Cdrs cdrs() {return new Cdrs();}
	@Bean public TelnetHelper telnetHelper() {return new TelnetHelperImpl();}
	
	@Bean
	MyThreadFactory threadFactory() {
		return new MyThreadFactory();
	}
	
	public class MyThreadFactory implements ThreadFactory {
		private MyUncaughtExceptionHandler handler = new MyUncaughtExceptionHandler();

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setUncaughtExceptionHandler(handler);
			return t;
		}
	}
	
	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(3);
//		executor.setMaxPoolSize(30);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("MyExecutor-");
		executor.setThreadFactory(threadFactory());
		executor.initialize();
		return executor;
	}
	
	@Bean
	public CompletionService<State> completionService() {
		CompletionService<State> service = new ExecutorCompletionService<State>(taskExecutor());
		return service;
	}
	
	@Bean(initMethod = "start")
	public TaskCompleter taskCompleter() {
		return new TaskCompleter(completionService(), taskExecutor());
	}
	
	@Bean
	public PeriodicTasks periodicTasks() {return new PeriodicTasks();}
	
	@Bean
	public HttpHelper httpHelper() {return new HttpHelper();}

//	 @Bean (initMethod="init")
//	 public CheckCDRTask checkCDRTask() {return new CheckCDRTask();}
	@Bean public SwitchTask switcher() {return new SwitchTask();}
	
	@Bean(initMethod = "start", destroyMethod = "stop")
	public AsteriskEvents asteriskEvents() {
		return mock(AsteriskEvents.class);
	}
	
	@Bean public CardsStates cardsStates() {return new CardsStates();}

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

