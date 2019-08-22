package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Dates {

    private static DateTimeFormatter fullDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String format(LocalDateTime dateTime) {
        return dateTime.format(fullDateTime);
    }
}
