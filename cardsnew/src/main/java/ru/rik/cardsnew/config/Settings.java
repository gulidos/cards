package ru.rik.cardsnew.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.inlinetelecom.commons.settings.OracleSettingsStorage;
import ru.inlinetelecom.commons.settings.Setting;
import ru.inlinetelecom.commons.settings.SettingListElement;
import ru.inlinetelecom.commons.settings.SettingsStorage;
import ru.inlinetelecom.commons.settings.exceptions.SettingsException;

//@Component
public class Settings {

	public static final String DS_JNDI = "java:comp/env/jdbc/blmgmn";
	// public static final String DS_JNDI = "jdbc/blmgmn";
	private static final Logger logger = LoggerFactory.getLogger(Settings.class);
	@Setting(description = "хост сервера UDP")
	public static volatile String UDP_SERVER_HOST = "127.0.0.1";
	@Setting(description = "порт")
	public static volatile int UDP_SERVER_PORT = 8877;
	@Setting(description = "число тредов")
	public static volatile int UDP_PROCESSING_THREADS = 15;
	@Setting(description = "Максимальный размер очереди UDP")
	public static volatile int UDP_MAX_QUEUE_SIZE = 2000;
	@Setting(description = "Интервал времени для проверки worker threads")
	public static volatile int TICK_PERIOD = 60000;
	@Setting(description = "размер пакета")
	public static volatile int UDP_MESSAGE_MAX_SIZE = 1412;
	@Setting(description = "Префикс для отсуствующих в белом листе номеров")
	public static volatile String PREF_NE = "999";
	@Setting(description = "Префикс для добавления к В номеру, если А номер в БЛ для МН")
	public static volatile String PREF_INT_BL = "999";
	@Setting(description = "Префикс для добавления к В номеру, если А номер в БЛ для МГ")
	public static volatile String PREF_NAT_BL = "999";
	@Setting(description = "ServiceKey для МН вызовов")
	public static volatile int SK_INT = 100;
	@Setting(description = "ServiceKey для МГ вызовов")
	public static volatile int SK_NAT = 200;

	public static DataSource dataSource;
	
	//@Autowired
	public Settings (DataSource dataSource) throws SettingsException, SQLException {
		Settings.dataSource = dataSource;
		//loadParams();
		System.out.println("Settings was created !!!");
	}
	
	public void loadParams() throws SettingsException, SQLException {
		try {
			OracleSettingsStorage storage = new OracleSettingsStorage(dataSource);
			DataSource ds = storage.getDataSource();

			Connection conn = ds.getConnection();
			storage.setNameField("NAME");
			storage.setValueFiled("VALUE");
			storage.setDescriptionField("DESCRIPTION");
			storage.load(Settings.class);
			logger.info("settings loaded");
			for (SettingListElement sle : SettingsStorage.getSettingsList(Settings.class)) {
				logger.info(sle.getSettingName() + " = " + sle.getSettingValue() + " (" + sle.getSettingDescription()
						+ ")");
			}
		} catch (SettingsException se) {
			logger.error("faield to load settings for JNDI/JDBC pool =", se);
			throw se;
		}
	}



	public static String getParameters() {
		StringBuffer sb = new StringBuffer();

		try {
			List<SettingListElement> result = SettingsStorage.getSettingsList(Settings.class);
			for (SettingListElement setting : result) {
				sb.append(String.format("%1$s = %2$s (%3$s)\n", setting.getSettingName(), setting.getSettingValue(),
						setting.getSettingDescription()));
			}
			return sb.toString();
		} catch (SettingsException e) {
			logger.error(e.toString(), e);
			return null;
		}

	}
}
