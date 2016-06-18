package ru.rik.cardsnew.config;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

@Configuration
@Import(ru.rik.cardsnew.db.JpaConfig.class)
//@ComponentScan(basePackages = { "ru.rik.cardsnew" }, excludeFilters = {
//		@Filter(type = FilterType.CUSTOM, value = WebPackage.class) })

public class RootConfig {
	private static final Logger logger = LoggerFactory.getLogger(RootConfig.class);

	public static class WebPackage extends RegexPatternTypeFilter {
		public WebPackage() {
			super(Pattern.compile("ru.rik.cardsnew\\.web"));
		}
	}

	
	
	

	
}
