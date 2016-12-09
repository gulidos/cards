package ru.rik.cardsnew;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.sync.NoOpSynchronizer;
import org.apache.commons.configuration2.sync.ReadWriteSynchronizer;

public class CommonConfigTest {

	public CommonConfigTest() {
	}

	public static void main(String[] args) {
		Configurations configs = new Configurations();
		try {
			Configuration config = configs.properties(new File("~/application.properties"));
			
			String dbHost = config.getString("database.host");
			int dbPort = config.getInt("database.port");
			String dbUser = config.getString("database.user");
			String dbPassword = config.getString("database.password", "secret");  // provide a default
			long dbTimeout = config.getLong("database.timeout");
			System.out.println(dbHost + " " + dbPort);
			
			config.setSynchronizer(new ReadWriteSynchronizer());
			
			config.setProperty("database.host", "sasdfasdf");
	
			config.setSynchronizer(NoOpSynchronizer.INSTANCE );
			System.out.println(config.getString("database.host"));
			
			
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

}
