package ru.rik.cardsnew.config;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AppInitializer implements ApplicationContextAware{

	private static ApplicationContext context;
	

	public AppInitializer() {	}

	
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
