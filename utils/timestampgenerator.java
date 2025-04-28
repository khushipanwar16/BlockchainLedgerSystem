package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

// A simple timestamp generator
public class TimestampGenerator {

    // Generate the current timestamp in "yyyy-MM-dd HH:mm:ss" format
    public static String getCurrentTimestamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }
}
