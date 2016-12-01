package ru.rik.cardsnew.config;

public class Settings {
	public static volatile String MSK_REGCODE = "77";
    public static volatile int ASR_AFFECTED = 10;
    public static volatile int ACD_AFFECTED = 10;

    public static volatile int NORM_INTERVAL = 190;
    
    public static volatile int FAIL_INTERVAL = 20;

    public static volatile String FAKE_BANK_IP = "192.168.99.99";
    public static volatile String FAKE_CARD_PLACE = "b0000099";
    // max time for changing card and rebooting Sec
    public static volatile int TIME_FOR_SWITCH = 300;
    public static volatile int PERCENT_MSK_PLUS = 30;
    public static volatile int MAX_OFFNET_MIN = 2;
    public static volatile int MAX_SMS_FETCH_PERIOD = 600;
    
    public static volatile int MAX_BALANCE_CHECK_PERIOD = 24*60*60;
    public static volatile float MIN_AVAILABLE_BALANCE = 10;
    
    public static volatile String CHECK_BALANCE_USSD = "*100#";
	
	public Settings() {
	}

}
