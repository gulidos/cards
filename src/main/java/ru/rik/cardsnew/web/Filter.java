package ru.rik.cardsnew.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import lombok.Data;

@Component(value="filter")
@Scope(value=WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class Filter {
	private static final Logger logger = LoggerFactory.getLogger(Filter.class);		
	
	private String url;
	private long id;
	
	public Filter() {
		logger.debug("filter create in the session scope");
	}

}
