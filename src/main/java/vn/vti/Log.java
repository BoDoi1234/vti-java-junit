package vn.vti;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    public static void info(Class<?> cl, Object... msg) {
        Logger log = LogManager.getLogger(cl);

        Object result = "";
        for (Object ob : msg) {
            result = String.format("%s %s", result, ob).trim();
        }

        log.info(result);
    }
}
