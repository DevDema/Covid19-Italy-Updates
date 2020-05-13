package net.ddns.andrewnetwork.helpers.util.time;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static long DATE_TIME_24 = 24*60*60*1000;

    public static Date twoHourAfter(Date date) {
        return minutesAfter(date, 120);
    }

    public static Date minutesAfter(Date date, int minutes) {
        Calendar calendar = toCalendar(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static void setMidnight(Date date) {
        Calendar calendar = toCalendar(date);

        setMidnight(calendar);
    }

    public static void setMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        setMidnight(date1);
        setMidnight(date2);

        return date1.getTime() == date2.getTime();
    }

    public static boolean isSameDay(Calendar date1, Calendar date2) {
        return isSameDay(date1.getTime(), date2.getTime());
    }

    public static boolean isTomorrowDay(Date date1, Date date2) {
        setMidnight(date1);
        setMidnight(date2);

        return Math.abs(date1.getTime() - date2.getTime()) == DATE_TIME_24;
    }

    public static boolean isTomorrowDay(Calendar date1, Calendar date2) {
        return isTomorrowDay(date1.getTime(), date2.getTime())
    }

    public static Calendar toCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar;
    }
}
