package ru.rik.cardsnew.config;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

@Configuration
@EnableCaching
@Import(ru.rik.cardsnew.db.JpaConfig.class)
// @ComponentScan(basePackages = { "ru.rik.cardsnew" }, excludeFilters = {
// @Filter(type = FilterType.CUSTOM, value = WebPackage.class) })

public class RootConfig {
	private static final Logger logger = LoggerFactory.getLogger(RootConfig.class);

	public static class WebPackage extends RegexPatternTypeFilter {
		public WebPackage() {
			super(Pattern.compile("ru.rik.cardsnew\\.web"));
		}
	}

//	@Bean
//	@Autowired
//	public EhCacheCacheManager cacheManager(CacheManager cm) {
//		return new EhCacheCacheManager();
//	}

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
//	@Bean
//	public EhCacheManagerFactoryBean ehcache() {
//		EhCacheManagerFactoryBean ehCacheFactoryBean = new EhCacheManagerFactoryBean();
//		ehCacheFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
//		return ehCacheFactoryBean;
//	}

}
