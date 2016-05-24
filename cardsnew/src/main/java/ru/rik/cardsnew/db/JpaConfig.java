package ru.rik.cardsnew.db;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
@EnableJpaRepositories(basePackages="ru.rik.cardsnew.db")
public class JpaConfig {
	private static final String H2_JDBC_URL = "jdbc:h2:~/dbcards;mv_store=false";
	private static final String H2_JDBC_MEM = "jdbc:h2:mem:test_mem";
  @Bean
  public DataSource dataSource() {
//    EmbeddedDatabaseBuilder ds = new EmbeddedDatabaseBuilder();
//    edb.setType(EmbeddedDatabaseType.H2);
	  JdbcDataSource ds = new JdbcDataSource();
	  ds.setURL(H2_JDBC_URL);
	  ds.setUser("sa");
	  ds.setPassword("");
   // edb.addScript("schema.sql");
   // edb.addScript("spittr/db/jpa/test-data.sql"); 
//    EmbeddedDatabase embeddedDatabase = edb.build();
//    return embeddedDatabase;
	  return ds;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
    emf.setDataSource(dataSource);
    emf.setPersistenceUnitName("cards");
    emf.setJpaVendorAdapter(jpaVendorAdapter);
    emf.setPackagesToScan("ru.rik.cardsnew.domain");
    return emf;
  }
  
  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
    adapter.setDatabase(Database.H2);
    adapter.setShowSql(true);
    adapter.setGenerateDdl(true);
    adapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
    return adapter;
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
