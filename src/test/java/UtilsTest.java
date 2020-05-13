import net.ddns.andrewnetwork.helpers.util.time.DateUtil;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.logging.Logger;

public class UtilsTest {

    private static long DATE_TIME_24 = 24*60*60*1000;

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
        Calendar date = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date.set(Calendar.HOUR_OF_DAY, 17);
        date2.set(Calendar.HOUR_OF_DAY, 17);

        date2.add(Calendar.DAY_OF_MONTH, 1);

        assert DateUtil.isTomorrowDay(date, date2);
    }
}
