package ru.rik.cardsnew.config;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

import ru.rik.cardsnew.domain.repo.ChannelsStates;
import ru.rik.cardsnew.domain.repo.TrunksStates;
import ru.rik.cardsnew.service.http.HttpHelper;

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

	@Bean
	public AppInitializer appInitializer()  { 
		logger.debug("Instantiate the AppInitializer ...");
		return new AppInitializer();
	}
	
	@Bean
	public TrunksStates trunksStats()  { 
		logger.debug("Instantiate the TrunkStats ...");
		return new TrunksStates();
	}
	
	@Bean
	public ChannelsStates channelsStates()  { 
		logger.debug("Instantiate the ChannelsStates ...");
		return new ChannelsStates();
	}
	
	@Bean public HttpHelper httpHelper()  { return new HttpHelper();}
}
