import net.ddns.andrewnetwork.MainEntry;
import net.ddns.andrewnetwork.helpers.ApiHelper;
import net.ddns.andrewnetwork.helpers.TelegramHelper;
import net.ddns.andrewnetwork.helpers.util.CovidDataUtils;
import net.ddns.andrewnetwork.helpers.util.StringConfig;
import net.ddns.andrewnetwork.helpers.util.builder.ConfigDataBuilder;
import net.ddns.andrewnetwork.helpers.util.builder.ConfigSavedDataBuilder;
import net.ddns.andrewnetwork.helpers.util.time.DateUtil;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.util.*;
import java.util.logging.Logger;

public class DataGetTest {

    private final ApiHelper apiHelper = new ApiHelper();
    private static final CovidItaData today = new CovidItaData();
    private static final CovidItaData yesterday = new CovidItaData();
    private static final long channelId = -1001446903259L;
    private static final Set<Long> messagesToBeDeleted = new HashSet<>();

    @BeforeEach
    public void setupEach() {
        ConfigDataBuilder.clear();
    }

    @AfterAll
    public static void setupAfter() {
        ConfigDataBuilder.clear();

        for(long message : messagesToBeDeleted) {
            TelegramHelper.deleteMessage(message);
        }
    }

    @BeforeAll
    public static void setup() {
        TelegramHelper.setChannelId(channelId);

        Calendar todayCalendar = Calendar.getInstance();
        Calendar yesterdayCalendar = Calendar.getInstance();

        DateUtil.setMidnight(todayCalendar);
        DateUtil.setMidnight(yesterdayCalendar);

        todayCalendar.set(Calendar.DAY_OF_MONTH, 4);
        todayCalendar.set(Calendar.MONTH, Calendar.MAY);
        todayCalendar.set(Calendar.YEAR, 2020);
        todayCalendar.set(Calendar.HOUR_OF_DAY, 17);

        yesterdayCalendar.set(Calendar.DAY_OF_MONTH, 3);
        yesterdayCalendar.set(Calendar.MONTH, Calendar.MAY);
        yesterdayCalendar.set(Calendar.YEAR, 2020);
        yesterdayCalendar.set(Calendar.HOUR_OF_DAY, 17);

        today.setDate(todayCalendar.getTime());
        today.setTotalCases(211938); //4 MAY 2020
        today.setDeaths(29079);
        today.setTotalRecovered(82879);
        today.setTotalPositive(99980);
        today.setQuarantined(81678);
        today.setIntensiveCare(1479);
        today.setHospitalized(18302);
        today.setTestedPeople(1479910);
        today.setTests(2191403);
        today.setVariationPositive(-199);

        yesterday.setDate(yesterdayCalendar.getTime());
        yesterday.setTotalCases(210717); //3 MAY 2020
        yesterday.setDeaths(28884);
        yesterday.setTotalRecovered(81654);
        yesterday.setTotalPositive(100179);
        yesterday.setQuarantined(81436);
        yesterday.setIntensiveCare(1501);
        yesterday.setHospitalized(18743);
        yesterday.setTestedPeople(1456911);
        yesterday.setTests(2153772);
    }

    @Test
    public void getItalyData() {
        CovidItaData itaData = apiHelper.getItalyData();

        if(itaData != null) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Data: \n" + itaData.toString());
        }

        assert itaData != null && itaData.getCountryCode() != null && !itaData.getCountryCode().isEmpty();
    }

    @Test
    public void getRegionsData() {
        List<CovidRegionData> regionsData = apiHelper.getRegionsData();
        Set<CovidRegionData> newData = CovidDataUtils.getRegionByLabel(regionsData, "Lombardia", "Puglia");

        if(newData != null) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Data: \n" + newData.toString());
        }

        assert newData != null && !newData.isEmpty();
    }

    @Test
    public void computeVariationsOnSamples() {
        CovidDataUtils.computeVariations(today, yesterday);

        assert today.getVariationTotalCases() == 1221;
        assert today.getVariationDeaths() == 195;
        assert today.getVariationRecovered() == 1225;
        assert today.getVariationPositive() == -199; //OBVIOUS, BUT ANYWAY...
        assert today.getVariationIntensiveCare() == -22;
        assert today.getVariationHospitalized() == -441;
        assert today.getVariationTestedPeople() == 22999;
        assert today.getVariationTests() == 37631;
        assert today.getVariationQuarantined() == 242;
    }

    @Test
    public void editedMessageTest() {
        ConfigDataBuilder.clear();
        ConfigDataBuilder.getInstance()
                .getData()
                .putDays(ConfigSavedDataBuilder.getInstance()
                        .getLastData()
                        .putTodayData(yesterday, new HashSet<>())
                        .build()
                )
                .putChannelId(channelId)
                .commit();

        assert MainEntry.onDataLoaded(today, new HashSet<>());

        long messageId = ConfigDataBuilder.getConfigData().getMessageID();

        assert messageId != 0;

        today.setQuarantined(80650);

        CovidDataUtils.computeVariations(today, yesterday);

        assert MainEntry.onDataLoaded(today, new HashSet<>());

        assert messageId == ConfigDataBuilder.getConfigData().getMessageID();
        assert ConfigDataBuilder.getConfigData().getLastDay() != null && ConfigDataBuilder.getConfigData().getLastDay().getItalyDataSaved().getQuarantined() == 80650;

        messagesToBeDeleted.add(messageId);
    }

    @Test
    public void newMessageTest() {
        Calendar tomorrowCalendar = Calendar.getInstance(); //5 MAY 2020

        tomorrowCalendar.setTime(today.getDate());

        tomorrowCalendar.add(Calendar.DAY_OF_MONTH, 1);
        tomorrowCalendar.set(Calendar.HOUR_OF_DAY, 17);

        CovidItaData tomorrow = new CovidItaData();
        tomorrow.setDate(tomorrowCalendar.getTime());

        ConfigDataBuilder.clear();
        ConfigDataBuilder.getInstance()
                .getData()
                .putDays(ConfigSavedDataBuilder.getInstance()
                        .getLastData()
                        .putTodayData(yesterday, new HashSet<>())
                        .build()
                )
                .putChannelId(channelId)
                .commit();

        assert MainEntry.onDataLoaded(today, new HashSet<>());

        long messageId = ConfigDataBuilder.getConfigData().getMessageID();

        assert messageId != 0;

        assert MainEntry.onDataLoaded(tomorrow, new HashSet<>());

        long messageIdNew = ConfigDataBuilder.getConfigData().getMessageID();

        assert messageId != messageIdNew;

        messagesToBeDeleted.add(messageId);
    }

    @Test
    public void noMessageSentOnDifferentDays() {
        CovidRegionData covidRegionData = new CovidRegionData();
        CovidRegionData covidRegionData2 = new CovidRegionData();
        Set<CovidRegionData> covidRegionDataCollection = new HashSet<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today.getDate());
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        covidRegionData.setDate(calendar.getTime());
        covidRegionData2.setDate(calendar.getTime());

        covidRegionDataCollection.add(covidRegionData);
        covidRegionDataCollection.add(covidRegionData2);

        assert !MainEntry.onDataLoaded(today, covidRegionDataCollection);
    }

    @Test
    public void testSingulars() {
        CovidItaData covidItaData = new CovidItaData();

        covidItaData.setDate(Calendar.getInstance().getTime());

        covidItaData.setDeaths(1000);
        covidItaData.setVariationDeaths(1);

        covidItaData.setNewPositives(1);
        covidItaData.setVariationPositive(-1);

        String message = covidItaData.toString();

        assert message.contains("Morto");
        assert message.contains("Nuovo Caso");
        assert message.contains("Caso Positivo");

        covidItaData.setVariationPositive(-2);

        message = covidItaData.toString();

        assert !message.contains("Caso Positivo");
        assert message.contains("Casi Positivi");
    }
}
