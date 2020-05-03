package net.ddns.andrewnetwork.helpers.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static Date twoHourAfter(Date date) {
        return minutesAfter(date, 120);
    }

    public static Date minutesAfter(Date date, int minutes) {
        Calendar calendar = toCalendar(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }


    public static Calendar toCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
