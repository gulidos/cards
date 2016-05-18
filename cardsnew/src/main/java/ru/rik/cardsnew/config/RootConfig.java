package ru.rik.cardsnew.config;

import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

import ru.inlinetelecom.commons.settings.exceptions.SettingsException;
import ru.rik.cardsnew.config.RootConfig.WebPackage;

@Configuration
@Import(ru.rik.cardsnew.db.JpaConfig.class)
@ComponentScan(basePackages = { "ru.rik.cardsnew" }, excludeFilters = {
		@Filter(type = FilterType.CUSTOM, value = WebPackage.class) })

public class RootConfig {
	private static final Logger logger = LoggerFactory.getLogger(RootConfig.class);

	public static class WebPackage extends RegexPatternTypeFilter {
		public WebPackage() {
			super(Pattern.compile("ru.rik.cardsnew\\.web"));
		}
	}

	@Bean
	public Settings settings(DataSource dataSource) throws SettingsException, SQLException {
		return new Settings(dataSource);
	}
//
//	@Bean
//	public LoadMe loadMe(DataSource dataSource) {
//		LoadMe loadMe = null;
//		try {
//			loadMe = new LoadMe(dataSource);
//		} catch (Exception e) {
//			logger.error(e.toString(), e);
//		}
//		return loadMe;
//	}
}
