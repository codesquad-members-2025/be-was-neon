package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class MyLog {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(Object obj) {
        String time = LocalDateTime.now().format(dateTimeFormatter);
        System.out.printf("%s [%9s] %s\n",
                time,
                Thread.currentThread().getName(),
                obj);
    }
}
