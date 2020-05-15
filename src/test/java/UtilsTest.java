import net.ddns.andrewnetwork.helpers.util.time.DateUtil;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class UtilsTest {

    private static final long DATE_TIME_24 = 24 * 60 * 60 * 1000;

    @Test
    public void midnightDateTest() {
        Calendar date = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        DateUtil.setMidnight(date);
        DateUtil.setMidnight(date2);

        date2.add(Calendar.DAY_OF_MONTH, -1);

        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(date.getTime().getTime() - date2.getTime().getTime() + " ==? " + DATE_TIME_24);
        assert date.getTime().getTime() - date2.getTime().getTime() == DATE_TIME_24;
    }


    @Test
    public void isSameDayTest() {
        Calendar date = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date2.add(Calendar.DAY_OF_MONTH, -1);

        assert !DateUtil.isSameDay(date, date2);

        date2.add(Calendar.DAY_OF_MONTH, 1);

        assert DateUtil.isSameDay(date, date2);
    }

    @Test
    public void isTomorrow() {
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.set(Calendar.HOUR_OF_DAY, 17);
        date2.set(Calendar.HOUR_OF_DAY, 17);

        date2.add(Calendar.DAY_OF_MONTH, 1);

        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Date1: " + date1.getTime().getTime());
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Date2: " + date2.getTime().getTime());
        long subtraction = date2.getTime().getTime() - date1.getTime().getTime();
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Subtraction: " + subtraction);
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Result: " + (subtraction == DATE_TIME_24));

        assert DateUtil.isTomorrowDay(date1, date2);
    }

    @Test
    public void areSameDays() {
        Set<Date> dateSet = new HashSet<>();

        dateSet.add(Calendar.getInstance().getTime());
        dateSet.add(Calendar.getInstance().getTime());
        dateSet.add(Calendar.getInstance().getTime());

        assert DateUtil.areSameDays(dateSet);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        dateSet.add(calendar.getTime());

        assert !DateUtil.areSameDays(dateSet);
    }
}
