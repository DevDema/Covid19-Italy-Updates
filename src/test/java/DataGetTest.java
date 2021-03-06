import net.ddns.andrewnetwork.MainEntry;
import net.ddns.andrewnetwork.helpers.ApiHelper;
import net.ddns.andrewnetwork.helpers.TelegramHelper;
import net.ddns.andrewnetwork.helpers.async.AsyncCall;
import net.ddns.andrewnetwork.helpers.util.CovidDataUtils;
import net.ddns.andrewnetwork.helpers.util.StringConfig;
import net.ddns.andrewnetwork.helpers.util.builder.ConfigDataBuilder;
import net.ddns.andrewnetwork.helpers.util.builder.SavedDataBuilder;
import net.ddns.andrewnetwork.helpers.util.builder.SavedDataDayBuilder;
import net.ddns.andrewnetwork.helpers.util.time.DateUtil;
import net.ddns.andrewnetwork.model.CovidItaData;
import net.ddns.andrewnetwork.model.CovidRegionData;
import net.ddns.andrewnetwork.model.SavedData;
import net.ddns.andrewnetwork.model.SavedDataDay;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DataGetTest {

    private static final int ITALY_REGIONS_COUNT = 21; //INSTEAD OF 20 because Trento and Bolzano are separated.
    private final ApiHelper apiHelper = new ApiHelper();
    private static final CovidItaData today = new CovidItaData();
    private static final CovidItaData yesterday = new CovidItaData();
    private static final CovidRegionData todayRegionLombardia = new CovidRegionData();
    private static final CovidRegionData todayRegionPuglia = new CovidRegionData();
    private static final long channelId = -1001446903259L;
    private static final Set<Long> messagesToBeDeleted = new HashSet<>();

    @AfterAll
    public static void setupAfter() {
        SavedDataBuilder.clear();

        for (long message : messagesToBeDeleted) {
            TelegramHelper.deleteMessage(message);
        }
    }

    @BeforeAll
    public static void setup() {
        TelegramHelper.setChannelId(channelId);

        SavedDataBuilder.setSavedDataPath("saved-data-test.json");
        ConfigDataBuilder.setConfigPath("config-test.json");
        ConfigDataBuilder.clear();
        ConfigDataBuilder.getInstance()
                .getData()
                .putChannelId(channelId)
                .commit();

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

        todayRegionPuglia.setDate(todayCalendar.getTime());
        todayRegionPuglia.setTotalCases(210717); //3 MAY 2020 PUGLIA
        todayRegionPuglia.setDeaths(28884);
        todayRegionPuglia.setTotalRecovered(81654);
        todayRegionPuglia.setTotalPositive(100179);
        todayRegionPuglia.setQuarantined(81436);
        todayRegionPuglia.setIntensiveCare(1501);
        todayRegionPuglia.setHospitalized(18743);
        todayRegionPuglia.setTestedPeople(1456911);
        todayRegionPuglia.setTests(2153772);
        todayRegionPuglia.setRegionLabel("Puglia");

        todayRegionLombardia.setDate(todayCalendar.getTime());
        todayRegionLombardia.setTotalCases(210717); //3 MAY 2020 LOMBARDIA
        todayRegionLombardia.setDeaths(28884);
        todayRegionLombardia.setTotalRecovered(81654);
        todayRegionLombardia.setTotalPositive(100179);
        todayRegionLombardia.setQuarantined(81436);
        todayRegionLombardia.setIntensiveCare(1501);
        todayRegionLombardia.setHospitalized(18743);
        todayRegionLombardia.setTestedPeople(1456911);
        todayRegionLombardia.setTests(2153772);
        todayRegionLombardia.setRegionLabel("Lombardia");
    }

    @BeforeEach
    public void setupEach() {
        SavedDataBuilder.clear();
        ConfigDataBuilder.clearTemporaryData();

    }

    @Test
    public void getItalyData() throws IOException {
        CovidItaData itaData = apiHelper.getItalyData();

        if (itaData != null) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Data: \n" + itaData.toString());
        }

        assert itaData != null && itaData.getCountryCode() != null && !itaData.getCountryCode().isEmpty();
    }

    @Test
    public void getRegionsData() throws IOException {
        List<CovidRegionData> regionsData = apiHelper.getRegionsData();
        List<CovidRegionData> newData = CovidDataUtils.getRegionByLabel(regionsData, "Lombardia", "Puglia");

        if (newData != null) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Data: \n" + newData.toString());
        }

        assert newData != null && !newData.isEmpty();
    }

    @Test
    public void getAllRegionsData() {
        List<List<CovidRegionData>> iterableRegionsDate = AsyncCall.getAllRegionsData(new String[]{}).toList().toBlocking().first();

        assert !iterableRegionsDate.isEmpty();

        for (List<CovidRegionData> iterableRegions : iterableRegionsDate) {
            assert iterableRegions.size() == ITALY_REGIONS_COUNT;
        }

        assert iterableRegionsDate.stream().collect(Collectors.toMap(regionData -> regionData.get(0).getDate(), p -> p, (p, q) -> p)).size() == iterableRegionsDate.size();
    }

    @Test
    public void getAllRegionsDataFrom() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_MONTH, 15);
        calendar.set(Calendar.MONTH, 4); //MAY
        calendar.set(Calendar.YEAR, 2020);
        Date date = calendar.getTime();

        List<List<CovidRegionData>> iterableRegionsDate = AsyncCall.getAllRegionsDataFrom(new String[]{}, date).toList().toBlocking().first();

        assert !iterableRegionsDate.isEmpty();

        for (List<CovidRegionData> iterableRegions : iterableRegionsDate) {
            assert iterableRegions.size() == ITALY_REGIONS_COUNT;
        }

        assert iterableRegionsDate.stream().collect(Collectors.toMap(regionData -> regionData.get(0).getDate(), p -> p, (p, q) -> p)).size() == iterableRegionsDate.size();
        assert iterableRegionsDate.stream().map(regionData -> regionData.get(0).getDate()).allMatch(date1 -> date1.compareTo(date) >= 0);
    }

    @Test
    public void getAllItalyData() {
        List<CovidItaData> iterableItalyDate = AsyncCall.getAllItalyData().toList().toBlocking().first();

        assert !iterableItalyDate.isEmpty();

        assert iterableItalyDate.stream().collect(Collectors.toMap(CovidItaData::getDate, p -> p, (p, q) -> p)).size() == iterableItalyDate.size();
    }

    @Test
    public void getAllItalyDataFrom() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_MONTH, 15);
        calendar.set(Calendar.MONTH, 4); //MAY
        calendar.set(Calendar.YEAR, 2020);
        Date date = calendar.getTime();

        List<CovidItaData> iterableItalyDate = AsyncCall.getAllItalyDataFrom(date).toList().toBlocking().first();

        assert !iterableItalyDate.isEmpty();

        assert iterableItalyDate.stream().collect(Collectors.toMap(CovidItaData::getDate, p -> p, (p, q) -> p)).size() == iterableItalyDate.size();
        assert iterableItalyDate.stream().map(CovidItaData::getDate).allMatch(date1 -> date1.compareTo(date) >= 0);
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
    public void invalidNegativeData() {
        today.setDeaths(27000); //TESTED VALUE

        CovidDataUtils.computeVariations(today, yesterday);

        String finalMessage = StringConfig.buildFinalMessage(today.getDate(), today, new HashSet<>());

        assert finalMessage.contains("Possibile rimodulazione dati da parte del Ministero.");
    }

    @Test
    public void invalidNegativeDataOnRegions() {
        CovidRegionData covidRegionDataToday = new CovidRegionData();
        CovidRegionData covidRegionDataYesterday = new CovidRegionData();

        covidRegionDataToday.setRegionCode(0);
        covidRegionDataToday.setRegionLabel("Lombardia");
        covidRegionDataToday.setDate(today.getDate());
        covidRegionDataToday.setTotalCases(211938);
        covidRegionDataToday.setDeaths(27000); //TESTED VALUE
        covidRegionDataToday.setTotalRecovered(82879);
        covidRegionDataToday.setTotalPositive(99980);
        covidRegionDataToday.setQuarantined(81678);
        covidRegionDataToday.setIntensiveCare(1479);
        covidRegionDataToday.setHospitalized(18302);
        covidRegionDataToday.setTestedPeople(1479910);
        covidRegionDataToday.setTests(2191403);
        covidRegionDataToday.setVariationPositive(-199);

        covidRegionDataYesterday.setRegionCode(0);
        covidRegionDataYesterday.setRegionLabel("Lombardia");
        covidRegionDataYesterday.setDate(yesterday.getDate());
        covidRegionDataYesterday.setTotalCases(210717);
        covidRegionDataYesterday.setDeaths(28884); //TESTED VALUE
        covidRegionDataYesterday.setTotalRecovered(81654);
        covidRegionDataYesterday.setTotalPositive(100179);
        covidRegionDataYesterday.setQuarantined(81436);
        covidRegionDataYesterday.setIntensiveCare(1501);
        covidRegionDataYesterday.setHospitalized(18743);
        covidRegionDataYesterday.setTestedPeople(1456911);
        covidRegionDataYesterday.setTests(2153772);

        List<CovidRegionData> covidRegionTodayList = new ArrayList<CovidRegionData>() {{
            add(covidRegionDataToday);
        }};

        List<CovidRegionData> covidRegionYesterdayList = new ArrayList<CovidRegionData>() {{
            add(covidRegionDataYesterday);
        }};

        CovidDataUtils.computeVariationsList(covidRegionTodayList, covidRegionYesterdayList);

        String finalMessage = StringConfig.buildFinalMessage(today.getDate(), today, covidRegionTodayList);

        assert finalMessage.contains("Possibile rimodulazione dati da parte del Ministero.");
    }

    @Test
    public void getYesterdayTestOnEditedText() throws CloneNotSupportedException {
        CovidItaData todayBefore = yesterday.clone();

        Calendar todayCalendar = Calendar.getInstance();

        todayCalendar.set(Calendar.DAY_OF_MONTH, 4);
        todayCalendar.set(Calendar.MONTH, Calendar.MAY);
        todayCalendar.set(Calendar.YEAR, 2020);
        todayCalendar.set(Calendar.HOUR_OF_DAY, 17);

        todayBefore.setDate(todayCalendar.getTime());
        todayBefore.setQuarantined(80000);

        SavedDataDay savedDataDay1 = new SavedDataDay();
        savedDataDay1.setDate(yesterday.getDate());
        savedDataDay1.setItalyDataSaved(yesterday);
        savedDataDay1.setRegionsDataSaved(new HashSet<>());

        SavedDataDay savedDataDay2 = new SavedDataDay();
        savedDataDay2.setDate(todayBefore.getDate());
        savedDataDay2.setItalyDataSaved(todayBefore);
        savedDataDay2.setRegionsDataSaved(new HashSet<>());

        Collection<SavedDataDay> collection = new ArrayList<SavedDataDay>() {{
            add(savedDataDay1);
            add(savedDataDay2);
        }};

        todayCalendar = Calendar.getInstance();

        todayCalendar.set(Calendar.DAY_OF_MONTH, 4);
        todayCalendar.set(Calendar.MONTH, Calendar.MAY);
        todayCalendar.set(Calendar.YEAR, 2020);
        todayCalendar.set(Calendar.HOUR_OF_DAY, 19);

        today.setDate(todayCalendar.getTime());
        today.setQuarantined(80650);

        Calendar yesterdayCalendar = DateUtil.toCalendar(today.getDate());
        yesterdayCalendar.add(Calendar.DAY_OF_MONTH, -1);

        SavedData savedData = new SavedData(); //should never be done. Only for testing purposes.

        savedData.setLastDays(collection);

        assert savedData.getDayBy(yesterdayCalendar.getTime()).getItalyDataSaved().getQuarantined() == 81436;
    }

    @Test
    public void editedMessageTest() {
        SavedDataBuilder.clear();
        SavedDataBuilder.getInstance()
                .getData()
                .putDays(SavedDataDayBuilder.getInstance()
                        .getLastData()
                        .putTodayData(yesterday, new HashSet<>())
                        .build()
                )
                .commit();

        assert MainEntry.onDataLoaded(today, new HashSet<>());

        long messageId = ConfigDataBuilder.getMessageId();

        assert messageId != 0;

        today.setQuarantined(80650);

        CovidDataUtils.computeVariations(today, yesterday);

        assert MainEntry.onDataLoaded(today, new HashSet<>());

        assert messageId == ConfigDataBuilder.getMessageId();
        assert SavedDataBuilder.getSavedData().getLastDay() != null && SavedDataBuilder.getSavedData().getLastDay().getItalyDataSaved().getQuarantined() == 80650;

        messagesToBeDeleted.add(messageId);
    }

    @Test
    public void updateDataOnTheSameDay() {
        SavedDataBuilder.clear();
        SavedDataBuilder.getInstance()
                .getData()
                .putDays(SavedDataDayBuilder.getInstance()
                        .getLastData()
                        .putTodayData(yesterday, new HashSet<>())
                        .build()
                )
                .commit();
        Calendar newTodayCalendar = Calendar.getInstance();
        newTodayCalendar.setTime(today.getDate());
        newTodayCalendar.set(Calendar.HOUR_OF_DAY, 19);
        Date newToday = newTodayCalendar.getTime();

        Set<CovidRegionData> yesterdayCovidRegionDataSet = new HashSet<CovidRegionData>() {{
            add(todayRegionLombardia);
            add(todayRegionPuglia);
        }};

        assert MainEntry.onDataLoaded(today, yesterdayCovidRegionDataSet);

        long messageId = ConfigDataBuilder.getMessageId();
        assert messageId != 0;

        messagesToBeDeleted.add(messageId);

        CovidItaData covidItaData = AsyncCall.getItalyData().map(covidItaData1 -> {
            covidItaData1.setDate(newToday);
            return covidItaData1;
        }).toBlocking().value();

        assert covidItaData.getVariationDeaths() != 0;
        assert MainEntry.onDataLoaded(covidItaData, new HashSet<>());
        assert messageId == ConfigDataBuilder.getMessageId();

        messagesToBeDeleted.add(messageId);

        String[] regions = new String[]{"Lombardia", "Puglia"};
        List<CovidRegionData> covidRegionDataList = AsyncCall.getRegionsData(regions).map(covidRegionData -> {
            covidRegionData.forEach(covidRegionData1 -> covidRegionData1.setDate(newToday));

            CovidDataUtils.computeVariationsList(covidRegionData, yesterdayCovidRegionDataSet);
            return covidRegionData;
        }).toBlocking().value();

        covidRegionDataList.forEach(covidRegionData -> {
            assert covidRegionData.getVariationDeaths() != 0;
        });
        assert MainEntry.onDataLoaded(covidItaData, covidRegionDataList);
        assert messageId == ConfigDataBuilder.getMessageId();

        //assert that you store just one data for each day
        assert SavedDataBuilder.getSavedData().getLastDays().stream().filter(configSavedData -> DateUtil.isSameDay(configSavedData.getDate(), newToday)).count() == 1;

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

        SavedDataBuilder.clear();
        SavedDataBuilder.getInstance()
                .getData()
                .putDays(SavedDataDayBuilder.getInstance()
                        .getLastData()
                        .putTodayData(yesterday, new HashSet<>())
                        .build()
                )
                .commit();

        assert MainEntry.onDataLoaded(today, new HashSet<>());

        long messageId = ConfigDataBuilder.getMessageId();

        assert messageId != 0;

        assert MainEntry.onDataLoaded(tomorrow, new HashSet<>());

        long messageIdNew = ConfigDataBuilder.getMessageId();

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
