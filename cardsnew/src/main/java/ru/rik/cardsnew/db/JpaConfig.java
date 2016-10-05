package ru.rik.cardsnew.db;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan
@EnableTransactionManagement
@EnableCaching
@EnableJpaRepositories(basePackages="ru.rik.cardsnew.db")
public class JpaConfig {
//	private static final String H2_JDBC_URL = "jdbc:h2:~/dbcards;mv_store=false";
//	private static final String H2_JDBC_MEM = "jdbc:h2:mem:test_mem";
	private static final String MYSQL_JDBC_HP2 = "jdbc:mysql://127.0.0.1:3306/asterisk?autoReconnect=true&useSSL=false&characterEncoding=utf-8 ";

	  public DataSource dataSourceTarget() {
//	    EmbeddedDatabaseBuilder ds = new EmbeddedDatabaseBuilder();
//	    edb.setType(EmbeddedDatabaseType.H2);
//		  JdbcDataSource ds = new JdbcDataSource();
		  DriverManagerDataSource ds = new DriverManagerDataSource();
		  ds.setDriverClassName("com.mysql.jdbc.Driver");
		  ds.setUrl(MYSQL_JDBC_HP2);
		  ds.setUsername("root");
		  ds.setPassword("parallaxtal");

	   // edb.addScript("schema.sql");
	   // edb.addScript("spittr/db/jpa/test-data.sql"); 
//	    EmbeddedDatabase embeddedDatabase = edb.build();
//	    return embeddedDatabase;
		  return ds;
	  }

	  @Bean
	  public DataSource dataSource() {
		  LazyConnectionDataSourceProxy dataSourceProxy = new LazyConnectionDataSourceProxy();
		  dataSourceProxy.setTargetDataSource(dataSourceTarget());
		  return dataSourceProxy;
	  }

  
  

  private Map<String,?> jpaProperties() {
	  Map<String,String> jpaPropertiesMap = new HashMap<String,String>(); 
//	  jpaPropertiesMap.put("hibernate.dialect","org.hibernate.dialect.H2Dialect"); 
//	  jpaPropertiesMap.put("hibernate.hbm2ddl.auto", "update");
	  jpaPropertiesMap.put("hibernate.cache.use_second_level_cache", "true");
	  jpaPropertiesMap.put("hibernate.cache.use_query_cache", "true");
	  jpaPropertiesMap.put("hibernate.cache.region.factory_class", 
			  "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
	  jpaPropertiesMap.put("net.sf.ehcache.configurationResourceName", "ehcache.xml");
	  jpaPropertiesMap.put("hibernate.cache.use_structured_entries","false");
	  jpaPropertiesMap.put("hibernate.generate_statistics","true");
	  jpaPropertiesMap.put("hibernate.cache.auto_evict_collection_cache","true"); //!!! without it collections caches dont refresh
	  return jpaPropertiesMap;
  }
  
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
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
