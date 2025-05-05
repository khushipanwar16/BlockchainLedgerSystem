package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampGenerator {

    public static String getCurrentTimestamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }
}
