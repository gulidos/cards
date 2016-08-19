package ru.rik.cardsnew;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.rik.cardsnew.config.AppInitializer;
import ru.rik.cardsnew.service.http.HttpHelper;

//import ru.rik.cardsnew.config.AppInitializer;
//import ru.rik.cardsnew.domain.repo.ChannelsStates;
//import ru.rik.cardsnew.domain.repo.TrunksStates;
//import ru.rik.cardsnew.service.http.HttpHelper;

@Configuration
public class Test2Config  {



	@Bean public AppInitializer appInitializer()  { return new AppInitializer();}
//	@Bean public TrunksStates trunksStats()  { return new TrunksStates();	}
	@Bean public HttpHelper httpHelper()  { return new HttpHelper();}
	
	
	

	
}
