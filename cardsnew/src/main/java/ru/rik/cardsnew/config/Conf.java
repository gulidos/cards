package ru.rik.cardsnew.config;

import java.io.File;
//import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class Conf {
	public Configuration conf;

	public Conf() {
		Configurations configs = new Configurations();
		
		try {
			File f = new File("/opt/cards/t.properties");
			conf = configs.properties(f);

		} catch (ConfigurationException cex) {
			cex.printStackTrace();
		}
	}
	public static void main(String[] args) {
		Conf c = new Conf();
		String dbHost = c.conf.getString("m1988");
		System.out.println(dbHost);
	}
}
