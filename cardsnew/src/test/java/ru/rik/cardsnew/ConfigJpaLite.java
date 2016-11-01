package ru.rik.cardsnew;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ru.rik.cardsnew.db.CardRepo;
import ru.rik.cardsnew.db.CardRepoImpl;

@EnableTransactionManagement
@EnableCaching
@Configuration
public class ConfigJpaLite {
	private static final String MYSQL_JDBC_HP2 = "jdbc:mysql://127.0.0.1:3306/asterisk?autoReconnect=true&useSSL=false&characterEncoding=utf-8 ";

	public ConfigJpaLite() {}
	
	public DataSource dataSourceTarget() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl(MYSQL_JDBC_HP2);
		ds.setUsername("root");
		ds.setPassword("parallaxtal");
		return ds;
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
		adapter.setDatabase(Database.MYSQL);
		adapter.setShowSql(true);
		adapter.setGenerateDdl(true);
		adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
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
	
//	@Bean public ChannelRepo chanRepo() {return new ChannelRepoImpl();}
	@Bean(initMethod = "init") 
	public CardRepo cardRepo() {return new CardRepoImpl();}

	// @Bean public TrunksStates trunksStats() { return new TrunksStates();
	// }
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

