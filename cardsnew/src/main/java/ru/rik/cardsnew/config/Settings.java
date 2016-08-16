package ru.rik.cardsnew.config;

import java.text.DecimalFormat;

public class Settings {
	public static volatile String MSK_REGCODE = "77";
    public static volatile int ASR_AFFECTED = 10;
//    @Setting(description = "MTS statistics window (minutes)")
    public static volatile int MTS_WINDOW = 3840;
//    @Setting(description = "Beeline statistics window (minutes)")
    public static volatile int BEELINE_WINDOW = 5760;
//    @Setting(description = "Megafon statistics window (minutes)")
    public static volatile int MEGAFON_WINDOW = 5760;
//    @Setting(description = "Максимальное коли-во свежих event в очереди при удалении")
    public static volatile int STOP_NUMBER = 5;

    
    
    public static final DecimalFormat df = new DecimalFormat("###.##");
    public static final DecimalFormat d_int = new DecimalFormat("##");
	
	public Settings() {
	}

}
