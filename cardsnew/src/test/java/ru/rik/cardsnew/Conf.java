package ru.rik.cardsnew;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedBuilderParametersImpl;
import org.apache.commons.configuration2.builder.ReloadingDetectorFactory;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;
import org.apache.commons.configuration2.reloading.ManagedReloadingDetector;
import org.apache.commons.configuration2.reloading.PeriodicReloadingTrigger;
import org.apache.commons.configuration2.reloading.ReloadingDetector;

public class Conf {
	ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> builder;
	
	public Conf() {
		File file = new File("~/application.properties");
		Parameters params = new Parameters();
		builder =
		    new ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		    		
		    		.setFile(file)
		    		.setReloadingDetectorFactory(new ReloadingDetectorFactory() {
						public ReloadingDetector createReloadingDetector(FileHandler handler, FileBasedBuilderParametersImpl params)
								throws ConfigurationException {
							return new ManagedReloadingDetector();
						}
					})
		     );
		
		 PeriodicReloadingTrigger trigger = new PeriodicReloadingTrigger(builder.getReloadingController(),  null, 1, TimeUnit.MINUTES);
		
	}
	
	
	public Configuration reloadConfigurationFile() {
		boolean isReloadSuccessful = builder.getReloadingController().checkForReloading(null);
		Configuration config = null;
		if (isReloadSuccessful) {
			try {
				 config = builder.getConfiguration();
			} catch (Exception e) {
				e.printStackTrace();
				isReloadSuccessful = false;
			}
		}

		return config;
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		Conf conf = new Conf();
		try {
			Configuration config = conf.builder.getConfiguration(); 
			
			String dbHost = config.getString("database.host");
			int dbPort = config.getInt("database.port");
			System.out.println(dbHost + " " + dbPort);
			
			Thread.sleep(5000);

//			 
//			ReloadingController controller = conf.builder.getReloadingController();
//			ManagedReloadingDetector strategy = (ManagedReloadingDetector) controller.getDetector();
//			strategy.refresh();
//			
			
			config = conf.reloadConfigurationFile(); 
//					conf.builder.getConfiguration(); 

			System.out.println(config.getString("database.host") + " " + config.getInt("database.port"));
			
			
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

}
