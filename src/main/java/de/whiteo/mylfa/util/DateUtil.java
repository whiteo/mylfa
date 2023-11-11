package de.whiteo.mylfa.util;

import java.time.LocalDateTime;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

public class DateUtil {

    public static final String PATTERN_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static LocalDateTime setCorrectTime(LocalDateTime localDateTime, boolean isStartOfDay) {
        if (null == localDateTime) {
            return null;
        }

        if (isStartOfDay) {
            return localDateTime.toLocalDate().atStartOfDay();
        } else {
            return localDateTime.toLocalDate().atStartOfDay().plusDays(1).minusNanos(1);
        }
    }
}