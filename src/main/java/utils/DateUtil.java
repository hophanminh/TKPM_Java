package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final String PATTERN = "dd-MM-yyyy HH:mm:ss";
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);

    public static String formatDate(LocalDateTime dateTime){
        return formatter.format(dateTime);
    }

    public static LocalDateTime stringToDateTime(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        return dateTime;
    }

}
