package me.vtag.app.helpers;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by nageswara on 5/19/14.
 */
public class StringUtil {
    public static String durationFromSeconds(int seconds) {
        int hr = (int)(seconds/3600);
        int rem = (int)(seconds%3600);
        int mn = rem/60;
        int sec = rem%60;
        String hrStr =  hr > 0 ? (hr<10 ? "0" : "") + hr + ":" : "";
        String mnStr = (mn<10 ? "0" : "") + mn + ":";
        String secStr = (sec<10 ? "0" : "") + sec;
        return hrStr + mnStr + secStr;
    }

    public static String formatNumber(int number) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    }
}
