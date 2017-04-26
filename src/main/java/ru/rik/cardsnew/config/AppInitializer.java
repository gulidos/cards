package ru.rik.cardsnew.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AppInitializer implements ApplicationContextAware{

	private static ApplicationContext context;
	private static final Logger logger = LoggerFactory.getLogger(ApplicationContextAware.class);


	public AppInitializer() {
		logger.debug("Instantiate the AppInitializer ...");
	}

	
	public void initAllServices()  {
		
	}

	
	public void destroyAllServices() {
		
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		AppInitializer.context = context;
	}
	
	public static ApplicationContext getContext() {return context;	}
	
}
