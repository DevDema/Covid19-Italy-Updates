package net.ddns.andrewnetwork.helpers.util.time;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DateUtil {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final long DATE_TIME_24 = 24*60*60*1000;

    public static Date twoHourAfter(Date date) {
        return minutesAfter(date, 120);
    }

    public static Date minutesAfter(Date date, int minutes) {
        Calendar calendar = toCalendar(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static Date setMidnight(Date date) {
        Calendar calendar = toCalendar(date);

        setMidnight(calendar);

        return calendar.getTime();
    }

    public static void setMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null && date2 == null) {
            return true;
        }

        if (date1 == null || date2 == null) {
            return false;
        }

        Date date1cloned = (Date) date1.clone();
        Date date2cloned = (Date) date2.clone();
        date1cloned = DateUtil.setMidnight(date1cloned);
        date2cloned = DateUtil.setMidnight(date2cloned);

        return date1cloned.getTime() == date2cloned.getTime();
    }

    public static boolean isSameDay(Calendar date1, Calendar date2) {
        return isSameDay(date1.getTime(), date2.getTime());
    }

    public static boolean isTomorrowDay(Date date1, Date date2) {
        if (date2 == null || date1 == null) {
            throw new IllegalArgumentException("Date1 or Date2 are null.");
        }

        Date dateCloned1 = (Date) date1.clone();
        Date dateCloned2 = (Date) date2.clone();

        dateCloned1 = DateUtil.setMidnight(dateCloned1);
        dateCloned2 = DateUtil.setMidnight(dateCloned2);

        return Math.abs(dateCloned1.getTime() - dateCloned2.getTime()) == DATE_TIME_24;
    }

    public static boolean isTomorrowDay(Calendar date1, Calendar date2) {
        return isTomorrowDay(date1.getTime(), date2.getTime());
    }

    public static Calendar toCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar;
    }

    public static Date max(Date... dates) {
        return Stream.of(dates).max(Date::compareTo).orElse(null);
    }

    public static Date max(Date date, Collection<Date> dates) {
        dates.add(date);

        return max(dates);
    }

    public static Date max(Collection<Date> dates) {
        return dates.stream().max(Date::compareTo).orElse(null);
    }

    public static boolean areSameDays(Collection<Date> newDates) {
        Collection<Calendar> calendars = newDates.stream().map(DateUtil::toCalendar).collect(Collectors.toSet());

        return calendars.stream().peek(DateUtil::setMidnight).map(Calendar::getTime).map(Date::getTime).distinct().count() == 1;
    }
}
